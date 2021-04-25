package cn.edu.csust.coursecard.interceptor;

import cn.edu.csust.coursecard.service.JwService;
import cn.edu.csust.coursecard.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * websocket握手拦截器，主要用于websocket连接的身份验证
 *
 * @author zsw
 * @date 2021/4/21 15:03
 */
@Component
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {


    @Autowired
    RedisUtil redisUtil;


    /**
     * 握手之前。若返回false则不建立连接，若返回true则建立连接
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("websocket连接握手");

        ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
        String token = servletServerHttpRequest.getServletRequest().getParameter("token");

        Object userId = redisUtil.get(JwService.USER_TOKEN_PREFIX + token);
        if (token == null || "".equals(token.trim()) || userId == null) {
//            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "尚未登录");
            System.out.println("尚未登录");
            return false;
        }
        Object realToken = redisUtil.get(JwService.USER_ID_PREFIX + userId);
        if (!token.equals(realToken)) {
//            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "客户端数量达到上限");
            System.out.println("登上失效");
            return false;
        }

        Object stuId = redisUtil.get(JwService.USER_STUID_PREFIX + Integer.parseInt(userId.toString()));
        redisUtil.set(JwService.USER_ID_PREFIX + userId.toString(), token, JwService.TOKEN_EXPIRE);
        redisUtil.set(JwService.USER_TOKEN_PREFIX + token, userId.toString(), JwService.TOKEN_EXPIRE);
        redisUtil.set(JwService.USER_STUID_PREFIX + userId, stuId.toString(), JwService.TOKEN_EXPIRE);
        attributes.put("userId", userId);
        attributes.put("stuId", stuId);
        return true;
    }

    /**
     * 握手成功之后调用该方法
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
