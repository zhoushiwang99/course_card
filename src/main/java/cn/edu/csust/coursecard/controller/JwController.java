package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.service.JwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsw
 * @date 2019/11/18 21:40
 */
@RestController
public class JwController {

    @Autowired
    JwService jwService;

    @PostMapping("/login")
    public ReturnData login(HttpServletRequest request, @RequestParam("username") String username, @RequestParam("password") String password,String agent){
        return jwService.login(request, username, password,agent);
    }

    @PostMapping("/getStuInfo")
    public ReturnData getStuInfo(HttpServletRequest request,@RequestParam("cookie") String cookie){
        return jwService.getStuInfo(request, cookie);
    }

    @PostMapping("/getWeekDate")
    public ReturnData getWeekDate(HttpServletRequest request,@RequestParam("cookie")String cookie){
        return jwService.getWeekDate(request, cookie);
    }

    @PostMapping("/getWeekCourse")
    public ReturnData getWeekCourse(HttpServletRequest request, @RequestParam("cookie")String cookie, @RequestParam("time")@DateTimeFormat(pattern = "yyyy-MM-dd")String weekTime){
        return jwService.getWeekCourse(request, cookie, weekTime);
    }

    @PostMapping("/getCourse")
    public ReturnData getCourse(HttpServletRequest request,@RequestParam("cookie") String cookie,@RequestParam("xueqi")String xueqi,@RequestParam("zc")int zc){
        return jwService.getCourse(request, cookie, xueqi, zc);
    }

    @GetMapping("/getAllSemester")
    public ReturnData  getAllSemester(HttpServletRequest request){
        return jwService.getAllXueqi(request);
    }

    @PostMapping("/getKsap")
    public ReturnData getKsap(HttpServletRequest request,@RequestParam("cookie") String cookie,@RequestParam("xueqi")String  xueqi){
        return jwService.getKsap(request, cookie, xueqi);
    }

    @PostMapping("/queryScore")
    public ReturnData queryScore(HttpServletRequest request,@RequestParam("cookie") String cookie,@RequestParam("xueqi")String xueqi){
        return jwService.queryScore(request, cookie, xueqi);
    }

    @PostMapping("/queryPscj")
    public ReturnData queryPscj(HttpServletRequest request,@RequestParam("cookie")String cookie,@RequestParam("pscjUrl")String pscjUrl){
        return jwService.queryPscj(request, cookie, pscjUrl);
    }


}
