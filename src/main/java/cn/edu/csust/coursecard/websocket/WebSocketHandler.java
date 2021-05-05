package cn.edu.csust.coursecard.websocket;

import cn.edu.csust.coursecard.bean.Message;
import cn.edu.csust.coursecard.bean.UserNoticeRead;
import cn.edu.csust.coursecard.dao.MessageDAO;
import cn.edu.csust.coursecard.dao.UserNoticeReadDAO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zsw
 * @date 2021/4/21 15:06
 */
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    UserNoticeReadDAO userNoticeReadDAO;

    /**
     * 当前在线人数
     */
    private static final AtomicInteger onlineNum = new AtomicInteger();


    private static final ConcurrentHashMap<Integer, WebSocketSession> SESSION_POOLS = new ConcurrentHashMap<>();

    public void sendMessageToOne(Message message) {
        // 标记消息内容为未读
        message.setStatus(0);
        if (!checkMessageParam(message)) {
            log.error("消息内容验证失败" + message);
            return;
        }
        Integer toId = message.getToId();
        if (!SESSION_POOLS.containsKey(toId)) {
            log.info("当前用户不在线" + toId);
            messageDAO.insert(message);
            return;
        }
        WebSocketSession userSession = SESSION_POOLS.get(toId);
        try {
            userSession.sendMessage(new TextMessage(JSON.toJSONString(message)));
            // 标记消息内容为已读
            message.setStatus(1);
            messageDAO.insert(message);
        } catch (Exception e) {
            log.error("发送消息失败");
            messageDAO.insert(message);
        }
    }

    /**
     * 给所有在线的用户发送消息
     *
     * @param message
     */
    public void sendMessageToAllOnline(Message message) {
        for (Map.Entry<Integer, WebSocketSession> entry : SESSION_POOLS.entrySet()) {
            // 每次发送都要标记消息为未读
            message.setStatus(0);
            try {
                //设置toID
                message.setToId(entry.getKey());
                message.setCreateTime(new Date());
                message.setConversationId(message.getFromId() > message.getToId() ? message.getToId() + "_" + message.getFromId() : message.getFromId() + "_" + message.getToId());
                // 发送消息
                entry.getValue().sendMessage(new TextMessage(JSON.toJSONString(message)));
                // 发送成功，设置消息为已读
                message.setStatus(1);
                if (message.getNoticeId() != null) {
                    userNoticeReadDAO.insert(UserNoticeRead.builder().noticeId(message.getNoticeId()).userId(entry.getKey()).readTime(new Date()).build());
                }
                messageDAO.insert(message);
            } catch (IOException e) {
                log.error("发送消息失败");
                messageDAO.insert(message);
            }
        }
    }


    private static boolean checkMessageParam(Message message) {
        if (message == null || message.getToId() == null || message.getFromId() == null || message.getContent() == null || message.getContent().isEmpty()) {
            return false;
        }
        if (message.getCreateTime() == null) {
            message.setCreateTime(new Date());
        }
        return true;
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(session.getAttributes().get("userId") + "发来消息：" + message.getPayload());
    }

    /**
     * 连接建立后调用此方法
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        Integer userId = Integer.valueOf((String) attributes.get("userId"));
        log.info("用户" + userId + "已连接");
        onlineNum.incrementAndGet();
        // 将当前session加入会话池
        SESSION_POOLS.put(userId, session);
    }

    /**
     * 连接断开后调用此方法
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 在线人数减一
        onlineNum.decrementAndGet();
        Map<String, Object> attributes = session.getAttributes();
        Integer userId = Integer.valueOf((String) attributes.get("userId"));
        SESSION_POOLS.remove(userId);
    }
}
