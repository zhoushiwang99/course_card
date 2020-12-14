package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.bean.Advice;
import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.service.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author zsw
 * @date 2019/12/18 16:03
 */
@Validated
@RestController
public class AdviceController {

	@Autowired
	AdviceService adviceService;

	@Valid
	@PostMapping("/advice")
	public ReturnData addAdvice(HttpServletRequest request, @Validated Advice advice){
		return adviceService.addAdvice(request, advice);
	}


}
