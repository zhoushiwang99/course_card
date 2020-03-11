package cn.edu.csust.coursecard.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author zsw
 * @descirption
 * @date 2019/11/19 16:30
 */
@Mapper
public interface HeadImgDAO {
    String TABLE_NAME = "head_img";


    /**
     * 更新或者新增头像
     * @param userId
     * @param path
     * @return
     */
    @Update({"replace into",TABLE_NAME,"(user_id,head_path) values (#{userId},#{path})"})
    int updateHeadImg(Integer userId,String path);

    /**
     * 根据用户id获取用户头像路径
     * @param userId
     * @return
     */
    @Select({"select head_path from",TABLE_NAME,"where user_id = #{userId}"})
    String getHeadImgPathByUserId(Integer userId);
}
