package cn.edu.csust.coursecard.service;

/**
 * @author zsw
 * @date 2021/4/20 22:11
 */
public interface MessageService {


    /**
     * 查询最近未读消息
     *
     * @param userId
     * @return
     */
    void queryUnReadMessage(Integer userId);


}
