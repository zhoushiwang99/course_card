package cn.edu.csust.coursecard.common;

import lombok.Data;

/**
 * @author zsw
 * @date 2019/11/04 20:23
 */
@Data
public class ReturnData {

    private Integer code;
    private String msg;
    private Object data;


    public ReturnData(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ReturnData(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ReturnData success(Object data){
        return new ReturnData(CodeEnum.REQUEST_SUCCEED.getCode(),"请求成功",data);
    }
    public static ReturnData success(){
        return new ReturnData(CodeEnum.REQUEST_SUCCEED.getCode(),"请求成功");
    }

    public static ReturnData fail(Integer code,String message){
        return new ReturnData(code,message);
    }


}
