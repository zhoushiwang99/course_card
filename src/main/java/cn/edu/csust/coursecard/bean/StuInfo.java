package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zsw
 * @date 2019/11/18 21:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StuInfo {
    private Integer id;
    private String name;
    private String stuId;
    private String college;
    private String major;
    private String className;
    private Date registerTime;
}
