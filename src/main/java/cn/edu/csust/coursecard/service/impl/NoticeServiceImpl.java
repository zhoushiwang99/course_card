package cn.edu.csust.coursecard.service.impl;

import cn.edu.csust.coursecard.bean.Message;
import cn.edu.csust.coursecard.bean.Notice;
import cn.edu.csust.coursecard.dao.NoticeDAO;
import cn.edu.csust.coursecard.dao.UserNoticeReadDAO;
import cn.edu.csust.coursecard.event.MessageProducer;
import cn.edu.csust.coursecard.service.NoticeService;
import cn.edu.csust.coursecard.bean.UserNoticeRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zsw
 * @date 2021/4/24 22:25
 */
@Component("noticeService")
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeDAO noticeDAO;

    @Autowired
    UserNoticeReadDAO userNoticeReadDAO;

    @Autowired
    MessageProducer messageProducer;

    @Async("asyncServiceExecutor")
    @Override
    public void queryRecentUnReadNotice(Integer userId) {
        List<Integer> noticesIdList = noticeDAO.selectRecent30DaysNoticeToAll();
        List<Integer> unReadNoticeIdList = new LinkedList<>();
        for (Integer noticeId : noticesIdList) {
            // 如果用户未读该通知
            if (userNoticeReadDAO.selectHasUserReadTheNotice(userId,noticeId)==null) {
                unReadNoticeIdList.add(noticeId);
            }
        }
        List<Notice> unReadNoticeList = noticeDAO.selectNoticeListByIdList(unReadNoticeIdList);
        for (Notice notice : unReadNoticeList) {
            Message message = Message.builder().toId(userId).fromId(0).content(notice.getContent()).status(0).createTime(notice.getCreateTime()).build();
            // 发送消息
            messageProducer.fireMessage("Notice",message);
            userNoticeReadDAO.insert(UserNoticeRead.builder().userId(userId).noticeId(notice.getId()).readTime(new Date()).build());
        }
    }
}
