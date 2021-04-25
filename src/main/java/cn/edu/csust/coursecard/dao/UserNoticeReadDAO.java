package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.UserNoticeRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserNoticeReadDAO {
    /**
     * 插入一条通知已读记录
     * @param record
     * @return
     */
    int insert(UserNoticeRead record);

    /**
     * 用户是否已读该通知
     * @param userId
     * @param noticeId
     * @return
     */
    Integer selectHasUserReadTheNotice(@Param("userId") Integer userId,@Param("noticeId") Integer noticeId);

}




