package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.LoginInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zsw
 * @descirption
 * @date 2020/03/01 18:04
 */
@Mapper
public interface LoginInfoDAO {

	String TABLE_NAME = "login_info";
	String INSERT_FIELDS = "user_id,login_time,agent";

	@Insert({"<script> " +
			"insert into",TABLE_NAME,"(",INSERT_FIELDS,") " +
			"values " +
			"<foreach item='info' collection = 'loginInfoList' separator=','>(#{info.userId},#{info.loginTime},#{info.agent})</foreach>" +
			"</script>"})
	void insertLoginInfos(@Param("loginInfoList") List<LoginInfo> loginInfoList);

}
