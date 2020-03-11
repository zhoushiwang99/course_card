package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.service.HeadImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zsw
 * @date 2019/11/19 16:21
 */
@RestController
public class HeadImgController {

    @Autowired
    HeadImgService headImgService;

    @PostMapping("/setHeadImg")
    public ReturnData setHeadImg(HttpServletRequest request,@RequestParam("img") MultipartFile file){
        return headImgService.setHeadImg(request, file);
    }

    @PostMapping("/getHeadImg")
    public ReturnData getHeadImg(HttpServletRequest request, HttpServletResponse response,String cookie) throws Exception {
        return headImgService.getHeadImg(request, response, cookie);
    }

}
