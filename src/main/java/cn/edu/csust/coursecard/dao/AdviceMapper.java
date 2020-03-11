package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.Advice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zsw
 * @descirption
 * @date 2019/12/18 16:20
 */
@Mapper
public interface AdviceMapper {

	String TABLE_NAME = "advice";
	String INSERT_FIELDS = "user_id,phone,name,content,time";

	@Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (#{userId},#{phone},#{name},#{content},#{time})"})
	int addAdvice(Advice advice);

}
