package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Entity generator.domain.Notice
 */
@Mapper
public interface NoticeDAO {


    int insert(Notice record);

    /**
     * 查询最近30天发布的通知的id
     * @return
     */
    List<Integer> selectRecent30DaysNoticeToAll();

    /**
     * 根据id列表查询对应notice列表
     * @param idList
     * @return
     */
    List<Notice> selectNoticeListByIdList(List<Integer> idList);
}




