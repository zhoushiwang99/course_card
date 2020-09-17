package cn.edu.csust.coursecard.task;

import cn.edu.csust.coursecard.bean.LoginInfo;
import cn.edu.csust.coursecard.dao.LoginInfoDAO;
import cn.edu.csust.coursecard.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zsw
 * @date 2019/11/10 11:11
 */
@Component
public class ScheduledTasks {


	@Autowired
	RedisUtil redisUtill;

	@Autowired
	LoginInfoDAO loginInfoDAO;

	public static String LOGIN_INFO_PREFIX = "login:list";

//	@Scheduled(cron = "0 0 2 * * ?")
	@Scheduled(fixedRate = 60 * 1000 * 30)
	public void test() {
		Object obj = redisUtill.getObject(LOGIN_INFO_PREFIX);
		if (obj != null) {
			List<LoginInfo> loginInfos = (List<LoginInfo>) obj;
			loginInfoDAO.insertLoginInfos(loginInfos);
			loginInfos.clear();
			redisUtill.setObject(LOGIN_INFO_PREFIX, loginInfos);
		}
	}

	@Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 60 * 24)
	public void test1() {
		System.out.println("登录信息列表初始化");
		if (redisUtill.getObject(LOGIN_INFO_PREFIX) == null) {
			List<LoginInfo> loginInfos = new LinkedList<>();
			redisUtill.setObject(LOGIN_INFO_PREFIX, loginInfos);
		}
	}
}
