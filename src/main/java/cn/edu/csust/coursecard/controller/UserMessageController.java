package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.service.MessageService;
import cn.edu.csust.coursecard.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsw
 * @date 2021/4/24 22:38
 */
@RestController
public class UserMessageController {


    @Autowired
    NoticeService noticeService;

    @Autowired
    MessageService messageService;

    // 查询未读消息
    @GetMapping("/getUnReadMessage")
    public ReturnData getUserUnReadMessage(HttpServletRequest request) {
        Integer userId = Integer.valueOf((String)request.getAttribute("userId"));
        noticeService.queryRecentUnReadNotice(userId);
        messageService.queryUnReadMessage(userId);
        return ReturnData.success();
    }


}
