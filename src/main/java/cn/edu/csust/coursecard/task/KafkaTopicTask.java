package cn.edu.csust.coursecard.task;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.CommandLineRunner;

import java.util.Arrays;

/**
 * @author zsw
 * @date 2021/4/24 22:58
 */
//@Component
public class KafkaTopicTask implements CommandLineRunner {

//    @Autowired
    private KafkaAdminClient kafkaAdminClient;

    @Override
    public void run(String... args) throws Exception {
        NewTopic noticeTopic = new NewTopic("Notice", 3, (short)1);
        NewTopic systemNoticeTopic = new NewTopic("SystemNotice", 3, (short)1);
        kafkaAdminClient.createTopics(Arrays.asList(noticeTopic,systemNoticeTopic));
    }
}
