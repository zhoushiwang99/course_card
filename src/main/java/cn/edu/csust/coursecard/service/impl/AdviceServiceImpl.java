package cn.edu.csust.coursecard.service.impl;

import cn.edu.csust.coursecard.bean.Advice;
import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.dao.AdviceMapper;
import cn.edu.csust.coursecard.service.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author zsw
 * @date 2019/12/18 16:18
 */
@Service("adviceService")
public class AdviceServiceImpl implements AdviceService {

	@Autowired
	AdviceMapper adviceMapper;

	@Override
	public ReturnData addAdvice(HttpServletRequest request, Advice advice) {
		Integer userId = Integer.parseInt(request.getAttribute("userId").toString());
		advice.setTime(new Date());
		advice.setUserId(userId);
		adviceMapper.addAdvice(advice);
		return ReturnData.success();
	}
}
