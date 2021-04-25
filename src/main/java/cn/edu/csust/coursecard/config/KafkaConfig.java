package cn.edu.csust.coursecard.config;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author zsw
 * @date 2021/4/22 19:11
 */
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String server;


    @Bean
    public KafkaAdminClient kafkaAdminClient() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers",server);
        return (KafkaAdminClient) KafkaAdminClient.create(properties);
    }
}
