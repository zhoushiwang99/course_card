package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.bean.Message;
import cn.edu.csust.coursecard.bean.Notice;
import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.dao.NoticeDAO;
import cn.edu.csust.coursecard.dao.StuInfoDAO;
import cn.edu.csust.coursecard.event.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author zsw
 * @date 2021/4/21 21:28
 */
@RestController
public class NoticeController {

    private static final String ADMIN_NOTICE_TOKEN = "admin_notice_token";

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    NoticeDAO noticeDAO;

    @Autowired
    StuInfoDAO stuInfoDAO;

    @PostMapping("/admin/sendNoticeToAll")
    public ReturnData sendNoticeToAll(@RequestParam("token") String token, String content) {
        if (!ADMIN_NOTICE_TOKEN.equals(token)) {
            return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "请校验token");
        }
        Notice notice = Notice.builder().content(content).createTime(new Date()).target("all").build();
        noticeDAO.insert(notice);
        Message message = Message.builder().fromId(0).createTime(new Date()).noticeId(notice.getId()).content(content).build();
        messageProducer.fireMessage("SystemNotice", message);
//        webSocketHandler.sendMessageToAllOnline(message);
        return ReturnData.success();
    }

    @PostMapping("/admin/sendNoticeToOne")
    public ReturnData sendNoticeToOne(@RequestParam("token") String token, @RequestParam("content") String content,
                                      @RequestParam("userId") Integer userId) {
        if (!ADMIN_NOTICE_TOKEN.equals(token)) {
            return ReturnData.fail(CodeEnum.REQUEST_FAILED.getCode(), "请校验token");
        }
        if (stuInfoDAO.selectIfUserIdExist(userId) == null) {
            return ReturnData.fail(401,"找不到该Id对应的用户");
        }
        Message message = Message.builder().fromId(0).toId(userId)
                .content(content).status(0).createTime(new Date()).build();
        messageProducer.fireMessage("Notice",message);
        return ReturnData.success();
    }

    @PostMapping("/admin/sendNoticeToOldVersionUser")
    public ReturnData sendNoticeToOldVersionUser(@RequestParam("token") String token, @RequestParam("content") String content,@RequestParam("version")String version) {
        List<Integer> userIdList = stuInfoDAO.selectOldAppUserIdList(version);
        for (Integer userId : userIdList) {
            Message message = Message.builder().fromId(0).toId(userId)
                    .content(content).status(0).createTime(new Date()).build();
            messageProducer.fireMessage("Notice",message);
        }
        return ReturnData.success();
    }




}
