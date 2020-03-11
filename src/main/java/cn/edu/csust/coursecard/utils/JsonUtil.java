package cn.edu.csust.coursecard.utils;


import com.alibaba.fastjson.JSON;

/**
 * @author zsw
 * @date 2019/11/10 16:04
 */
public class JsonUtil {

    static String objToJson(Object obj){

        return JSON.toJSONString(obj);
    }

}
