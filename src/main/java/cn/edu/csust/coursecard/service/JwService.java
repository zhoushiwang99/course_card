package cn.edu.csust.coursecard.service;

import cn.edu.csust.coursecard.common.ReturnData;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsw
 * @descirption
 * @date 2019/11/18 18:52
 */
public interface JwService {

    String USER_TOKEN_PREFIX = "user:token:";
    String USER_ID_PREFIX = "user:id:";
    String USER_STUID_PREFIX = "user:stuid:id:";
    long TOKEN_EXPIRE = 60 * 30;

    ReturnData getCourse(HttpServletRequest request, String cookie, String xueqi,int zc);


    /**
     * 登录
     * @param request
     * @param username
     * @param password
     * @return
     */
    ReturnData login(HttpServletRequest request,String username,String password,String agent);

    /**
     * 考试安排
     * @param request
     * @param cookie
     * @param xueqi
     * @return
     */
    ReturnData getKsap(HttpServletRequest request,String cookie,String  xueqi);

    /**
     * 查询成绩
     * @param request
     * @param cookie
     * @param xueqi
     * @return
     */
    ReturnData queryScore(HttpServletRequest request,String cookie,@RequestParam("xueqi")String xueqi);

    /**
     * 获取学生信息
     * @param request
     * @param cookie
     * @return
     */
    ReturnData getStuInfo(HttpServletRequest request,String cookie);

    /**
     *
     * 查询平时成绩
     * @param request
     * @param cookie
     * @param pscjUrl
     * @return
     */
    ReturnData queryPscj(HttpServletRequest request,String cookie,String pscjUrl);


    /**
     * 从教务系统获取学生照片
     * @param request
     * @param cookie
     * @return
     */
    byte[] getXszp(HttpServletRequest request, String cookie);


    /**
     * 获取所有学期
     * @param request
     * @return
     */
    ReturnData getAllXueqi(HttpServletRequest request);

    /**
     * 获取周和日期的对应
     * @param request
     * @param cookie
     * @return
     */
    ReturnData getWeekDate(HttpServletRequest request,String cookie);

    ReturnData getWeekCourse(HttpServletRequest request, String cookie,String weekTime);

}
