package cn.edu.csust.coursecard.config;

import cn.edu.csust.coursecard.interceptor.WebSocketHandShakeInterceptor;
import cn.edu.csust.coursecard.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author zsw
 * @date 2021/4/21 0:01
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketHandShakeInterceptor handShakeInterceptor;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
            webSocketHandlerRegistry
                    .addHandler(webSocketHandler,"/websocket")
                    .setAllowedOrigins("*")
                    .addInterceptors(handShakeInterceptor);

    }

}
