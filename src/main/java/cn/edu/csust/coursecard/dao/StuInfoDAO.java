package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.StuInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author zsw
 * @descirption
 * @date 2019/11/18 21:32
 */
@Mapper
public interface StuInfoDAO {

    String TABLE_NAME = "stu_info";
    String INSERT_FIELDS = "stu_id,name,college,major,class_name,register_time";
    String SELECT_FIELDS ="id,stu_id,name,college,major,class_name";

    /**
     * 插入一条学生信息
     * @param stuInfo
     * @return
     */
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{stuId},#{name},#{college},#{major},#{className},#{registerTime})"})
    int insertStuInfo(StuInfo stuInfo);

    /**
     * 根据学号查询学生信息
     * @param stuId
     * @return
     */
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where stu_id = #{stuId}"})
    StuInfo selectStuInfoByStuId(String stuId);
}
