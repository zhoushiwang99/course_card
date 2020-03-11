package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsw
 * @date 2019/11/21 20:54
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Exam {

    /**
     * 校区
     */
    private String campus;

    /**
     * 课程名
     */
    private String courseName;
    /**
     * 授课老师
     */
    private String teacher;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 考试地点
     */
    private String address;
    /**
     * 座位号
     */
    private String seatNumber;
    /**
     * 准考证号
     */
    private String ticketNumber;

}
