package cn.edu.csust.coursecard.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author zsw
 * @date 2019/12/18 16:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Advice {
	private Integer id;
	@NotBlank
	@Pattern(regexp = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$")
	private String phone;
	@NotBlank
	@Size(min = 2,max = 10)
	private String name;
	@NotBlank
	@Size(min = 5,max = 100)
	private String content;
	private Date time;
	private Integer userId;
}
