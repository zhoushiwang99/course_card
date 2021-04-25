package cn.edu.csust.coursecard.service.impl;

import cn.edu.csust.coursecard.bean.Message;
import cn.edu.csust.coursecard.dao.MessageDAO;
import cn.edu.csust.coursecard.event.MessageProducer;
import cn.edu.csust.coursecard.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsw
 * @date 2021/4/20 23:37
 */
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    MessageProducer messageProducer;

    /**
     * 查询该用户的最近未读消息并发送 使用线程池异步实现
     * @param userId
     * @return
     */
    @Async("asyncServiceExecutor")
    @Override
    public void queryUnReadMessage(Integer userId) {
        // 此处后期还需优化。避免使用for循环多次调用，应改为一次调用
        List<Message> messages = messageDAO.selectByToIdAndStatus(userId, 0);
        for (Message message : messages) {
            // 使用消息队列发送
            messageProducer.fireMessage("Notice",message);
        }
    }
}
