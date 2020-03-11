package cn.edu.csust.coursecard.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author zsw
 * @date 2019/6/21 16:49
 */
public class RandomNumberUtil {

    private static Random random;
    static {
        random = new Random();
    }

    /**
     * 生成 n 位随机数
     * @param n
     * @return
     */
    public static String digitsGeneration(int n){
        if(n <= 0){
            return "";
        }
        StringBuffer verificationCode = new StringBuffer();
        for(int i = 0;i < n;i++){
            int currentNum = random.nextInt(10);
            verificationCode.append(currentNum);
        }
        return verificationCode.toString();
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
