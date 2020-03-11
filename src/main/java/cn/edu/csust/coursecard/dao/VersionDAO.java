package cn.edu.csust.coursecard.dao;

import cn.edu.csust.coursecard.bean.Version;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zsw
 * @descirption
 * @date 2020/02/16 22:56
 */
@Mapper
public interface VersionDAO {

//	@Select("select top 1 * from version order by version_id desc")
	@Select("select * from version order by version_id desc limit 1")
	Version getLastVersion();

}
