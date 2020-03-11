package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zsw
 * @date 2019/11/19 17:37
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WeekDate {
    private Integer week;
    private String mondayDate;
}
