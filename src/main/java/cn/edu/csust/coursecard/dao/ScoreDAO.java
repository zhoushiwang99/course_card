package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.Score;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author zsw
 * @descirption
 * @date 2019/11/22 21:15
 */
@Mapper
public interface ScoreDAO {

    /**
     * 插入成绩
     */
    String TABLE_NAME = "score";
    String INSERT_FIELDS = "stu_id,xueqi,course_name,score,type,xuefen,point,method,property,nature";
    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values (#{stuId},#{score.xueqi},#{score.courseName},#{score.score},#{score.type},#{score.xuefen},#{score.point},#{score.method},#{score.property},#{score.nature})"})
    int insertScore(@Param("stuId") String stuId,@Param("score") Score score);


    /**
     * 查询成绩
     * @param stuId
     * @param courseName
     * @param type
     * @return
     */
    @Select({"select * from",TABLE_NAME,"where stu_id = #{stuId} and course_name = #{courseName} and type = #{type}"})
    Score selectScore(String stuId, String courseName, String type);


}
