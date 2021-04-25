package cn.edu.csust.coursecard.event;

import cn.edu.csust.coursecard.bean.Message;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zsw
 * @date 2021/4/20 22:10
 */
@Component
public class MessageProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void fireMessage(String topic, Message message) {
        kafkaTemplate.send(topic, JSON.toJSONString(message));
    }
}
