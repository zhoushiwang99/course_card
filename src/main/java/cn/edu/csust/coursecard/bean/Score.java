package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsw
 * @date 2019/11/22 20:10
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Score {
    /**
     * 学期
     */
    private String xueqi;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 考试分数
     */
    private String score;
    /**
     * 成绩标识
     */
    private String type;
    /**
     * 学分
     */
    private String xuefen;
    /**
     * 绩点
     */
    private String point;
    /**
     * 考试方式
     */
    private String method;
    /**
     * 必修 or 选修
     */
    private String property;
    /**
     * 课程性质
     */
    private String nature;

    /**
     *获取查平时成绩的连接
     */
    private String pscjUrl;
}
