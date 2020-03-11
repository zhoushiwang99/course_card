package cn.edu.csust.coursecard.utils;


import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @Auther: wyx
 * @Date: 2019-05-09 14:35
 * @Description: 根据 HttpServletRequest 获取用户 IP 地址
 */
@Slf4j
public class IpUtil {

    /**
     * @Description: 获取客户端IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
                }
                ip= inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

}
