package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageDAO {


    int insert(Message message);

    Message selectByPrimaryKey(Long id);

    List<Message> selectByToIdAndStatus(@Param("toId") Integer toId, @Param("status") Integer status);
}




