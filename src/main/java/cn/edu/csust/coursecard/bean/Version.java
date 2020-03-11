package cn.edu.csust.coursecard.bean;

import lombok.Data;

/**
 * @author zsw
 * @date 2020/02/16 22:54
 */
@Data
public class Version {

	/**
	 * 版本Id
	 */
	private Integer versionId;
	/**
	 * 版本名
	 */
	private String versionName;
	/**
	 * apk路径
	 */
	private String apkPath;
	/**
	 * 更新信息
	 */
	private String info;
	/**
	 * 更新时间
	 */
	private String updateTime;



}
