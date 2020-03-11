package cn.edu.csust.coursecard.service;

import cn.edu.csust.coursecard.common.ReturnData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author zsw
 * @descirption
 * @date 2019/11/19 16:23
 */
public interface HeadImgService {
    /**
     * 图片后缀
     */
    List<String> IMG_SUFFIX = Arrays.asList(".jpg","jpeg",".png",".gif");

    /**
     * 单个图片的最大大小
     */
    long MAX_IMG_SIZE = 1024 * 1024 * 8;

    ReturnData setHeadImg(HttpServletRequest request, MultipartFile file);

    /**
     * 获得头像
     * @param request
     * @param response
     * @param cookie
     */
    ReturnData getHeadImg(HttpServletRequest request, HttpServletResponse response, String cookie) throws Exception;

}
