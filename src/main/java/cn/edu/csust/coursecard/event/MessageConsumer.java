package cn.edu.csust.coursecard.event;

import cn.edu.csust.coursecard.bean.Message;
import cn.edu.csust.coursecard.websocket.WebSocketHandler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zsw
 * @date 2021/4/20 22:10
 */
@Component
@Slf4j
public class MessageConsumer {

    @Autowired
    WebSocketHandler webSocketHandler;

    // 此方法订阅 "SystemNotice"主题，即系统通知主题
    @KafkaListener(topics = "SystemNotice")
    public void sendSystemNotice(ConsumerRecord<String, String> record) {
        Message message = checkMessage(record);
        if (message != null) {
            webSocketHandler.sendMessageToAllOnline(message);
        }
    }

    @KafkaListener(topics = "Notice")
    public void sendMessageToOne(ConsumerRecord<String, String> record) {
        Message message = checkMessage(record);
        if (message != null && message.getToId() != null) {
            webSocketHandler.sendMessageToOne(message);
        }
    }

    private static Message checkMessage(ConsumerRecord<String, String> record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return null;
        }
        Message message = JSON.parseObject(record.value(), Message.class);
        if (message == null) {
            log.error("消息格式错误：" + record.value());
            return null;
        }
        return message;
    }


}
