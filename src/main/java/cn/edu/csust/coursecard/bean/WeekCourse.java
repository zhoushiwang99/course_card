package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsw
 * @date 2019/11/21 16:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WeekCourse {

    private String courseName;
    private String teacher;
    private String time;
    private String address;

}
