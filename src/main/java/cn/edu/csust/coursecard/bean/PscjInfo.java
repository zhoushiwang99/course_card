package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsw
 * @date 2020/03/01 21:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PscjInfo {
	/**
	 * 平时成绩
	 */
	private String pscj;
	/**
	 * 平时成绩比例
	 */
	private String pscjBL;

	/**
	 * 考试成绩
	 */
	private String kscj;

	/**
	 * 考试成绩比例
	 */
	private String kscjBL;

	/**
	 * 总成绩
	 */
	private String score;

}
