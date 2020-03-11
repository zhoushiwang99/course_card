package cn.edu.csust.coursecard.utils;


import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author zsw
 * @date 2019/11/09 21:10
 */
@Slf4j
public class FileUtil {

    public static String fileTransfer(MultipartFile file, String path, List<String> suffixList, long minSize, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "文件不能为空");
        }
        //获取原文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀名
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!suffixList.contains(suffixName) || file.getSize() < minSize || file.getSize() > maxSize) {
            throw new BaseException(CodeEnum.REQUEST_FAILED.getCode(), "文件参数错误");
        }
        //新的文件名
        String fileName = RandomNumberUtil.getUUID() + suffixName;
        //文件的保存路径
        String filePath = path + fileName;
        try {
            file.transferTo(new File(filePath));
            return fileName;
        } catch (IOException e) {
            log.error("----------->图片转存失败:" + e.getMessage());
            throw new BaseException(CodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
        }
    }
    public static boolean delFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
    public static byte[] fileToBytes(File file) throws Exception {
        try(FileInputStream fis = new FileInputStream(file)){
            byte[] bytesArray = new byte[(int) file.length()];
            fis.read(bytesArray);
            return bytesArray;
        }catch (Exception e){
            throw new Exception("文件不存在");
        }
    }

}
