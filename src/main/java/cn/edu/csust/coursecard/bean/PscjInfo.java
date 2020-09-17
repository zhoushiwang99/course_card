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
	 * 期末成绩
	 */
	private String qmcj;

	/**
	 * 期末成绩比例
	 */
	private String qmcjBL;
	/**
	 * 期中成绩
	 */
	private String qzcj;
	/**
	 * 期中成绩比例
	 */
	private String qzcjBL;

	/**
	 * 总成绩
	 */
	private String score;

}
