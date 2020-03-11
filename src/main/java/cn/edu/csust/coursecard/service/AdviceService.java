package cn.edu.csust.coursecard.service;

import cn.edu.csust.coursecard.bean.Advice;
import cn.edu.csust.coursecard.common.ReturnData;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zsw
 * @descirption
 * @date 2019/12/18 16:17
 */
public interface AdviceService {

	ReturnData addAdvice(HttpServletRequest request, Advice advice);


}
