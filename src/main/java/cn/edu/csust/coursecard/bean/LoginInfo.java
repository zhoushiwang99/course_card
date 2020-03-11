package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author zsw
 * @date 2020/03/01 17:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginInfo {
	private Integer id;
	private Integer userId;
	private Date loginTime;
	private String agent;
}
