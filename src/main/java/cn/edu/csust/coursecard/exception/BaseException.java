package cn.edu.csust.coursecard.exception;

import lombok.Getter;

/**
 * @author zsw
 * @date 2019/6/21 16:05
 */
@Getter
public class BaseException extends RuntimeException{
    private Integer code;
    private String msg;


    public BaseException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
