package cn.edu.csust.coursecard.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @author zsw
 * @date 2021/4/21 0:38
 */
@Data
@Builder
public class User {
    private Integer userId;
    private String stuId;
}
