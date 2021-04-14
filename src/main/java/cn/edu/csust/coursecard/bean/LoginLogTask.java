package cn.edu.csust.coursecard.bean;

import cn.edu.csust.coursecard.dao.LoginInfoDAO;

import java.util.Arrays;

/**
 * @author zsw
 * @date 2021/4/7 19:33
 */
public class LoginLogTask implements Runnable {
    LoginInfo loginInfo;

    LoginInfoDAO loginInfoDAO;

    public LoginLogTask(LoginInfo loginInfo, LoginInfoDAO loginInfoDAO) {
        this.loginInfo = loginInfo;
        this.loginInfoDAO = loginInfoDAO;
    }

    @Override
    public void run() {
        loginInfoDAO.insertLoginInfo(loginInfo);
    }
}
