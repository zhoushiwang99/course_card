package cn.edu.csust.coursecard.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zsw
 * @date 2019/11/09 22:50
 */
@Configuration
public class WebAppConfig {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 创建redisTemplate模板
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        // 关联 redisConnectionFactory
        template.setConnectionFactory(redisConnectionFactory);
        // 创建序列化类
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 设置可见度
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 启动默认的类型
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 6.序列化类，对象映射设置
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 7.设置 value 的转化格式和 key 的转化格式
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
