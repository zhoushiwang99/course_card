package cn.edu.csust.coursecard.interceptor;

import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.exception.BaseException;
import cn.edu.csust.coursecard.service.JwService;
import cn.edu.csust.coursecard.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zsw
 * @date 2019/11/18 22:35
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtil redisUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        Object userId = redisUtil.get(JwService.USER_TOKEN_PREFIX + token);
        if (token == null || "".equals(token.trim()) || userId == null) {
            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(),"尚未登录");
        }
        Object realToken = redisUtil.get(JwService.USER_ID_PREFIX + userId);
        if(!token.equals(realToken)){
            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(),"客户端数量达到上限");
        }

        Object stuId = redisUtil.get(JwService.USER_STUID_PREFIX + Integer.parseInt(userId.toString()));
        redisUtil.set(JwService.USER_ID_PREFIX+ userId.toString(), token,JwService.TOKEN_EXPIRE);
        redisUtil.set(JwService.USER_TOKEN_PREFIX + token, userId.toString(), JwService.TOKEN_EXPIRE);
        redisUtil.set(JwService.USER_STUID_PREFIX+userId,stuId.toString(),JwService.TOKEN_EXPIRE);

        request.setAttribute("userId",userId);
        request.setAttribute("stuId",stuId);
        return true;
    }
}
