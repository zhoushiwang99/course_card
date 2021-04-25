package cn.edu.csust.coursecard.utils;

import cn.edu.csust.coursecard.bean.User;
import org.springframework.stereotype.Component;

/**
 * @author zsw
 * @date 2021/4/21 0:38
 */
@Component
public class HostHolder {
    private static final ThreadLocal<User> USERS = new ThreadLocal<>();

    public void setUser(User user) {
        USERS.set(user);
    }

    public User getUser() {
        return USERS.get();
    }

    public void clear() {
        USERS.remove();
    }

}
