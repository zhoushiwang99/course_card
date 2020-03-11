package cn.edu.csust.coursecard.service.impl;

import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.dao.HeadImgDAO;
import cn.edu.csust.coursecard.service.HeadImgService;
import cn.edu.csust.coursecard.service.JwService;
import cn.edu.csust.coursecard.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * @author zsw
 * @date 2019/11/19 16:23
 */
@Slf4j
@Service("headImgService")
public class HeadImgServiceImpl implements HeadImgService {

    @Autowired
    HeadImgDAO headImgDAO;

    @Autowired
    JwService jwService;


    @Autowired
    public HeadImgServiceImpl(@Value("${my.image.head-image-direct}") String headImgDirect, @Value("${my.image.default-head-direct}") String defaultHeadImageDirect) {
        this.headImgDirect = headImgDirect;
        this.defaultHeadImageDirect = defaultHeadImageDirect;
        //创建默认头像的文件夹
        File defaultHeadImageDirectFile = new File(defaultHeadImageDirect);
        if (!defaultHeadImageDirectFile.exists()) {
            defaultHeadImageDirectFile.mkdirs();
        }
        //创建用户设置头像的文件夹
        File headImgDirectFile = new File(headImgDirect);
        if (!headImgDirectFile.exists()) {
            headImgDirectFile.mkdirs();
        }

    }

    private String defaultHeadImageDirect;

    private String headImgDirect;



    @Override
    public ReturnData setHeadImg(HttpServletRequest request, MultipartFile file) {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        String fileName = FileUtil.fileTransfer(file, headImgDirect, IMG_SUFFIX, 0, MAX_IMG_SIZE);
        String imgPathByUserId = headImgDAO.getHeadImgPathByUserId(userId);
        headImgDAO.updateHeadImg(userId, headImgDirect + fileName);
        if(imgPathByUserId != null){
            File fileFromDb = new File(imgPathByUserId);
            if (fileFromDb.exists()) {
                fileFromDb.delete();
            }
        }
        return ReturnData.success();
    }

    @Override
    public ReturnData getHeadImg(HttpServletRequest request, HttpServletResponse response, String cookie) throws Exception {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        String stuId = request.getAttribute("stuId").toString();
        String imgPathByUserId = headImgDAO.getHeadImgPathByUserId(userId);
        if (imgPathByUserId != null) {
            //如果设置过头像
            File file = new File(imgPathByUserId);
            byte[] bytes;
            if (file.exists()) {
                //本地头像存在
                bytes = FileUtil.fileToBytes(file);
            } else {
                File defaultFile = new File(defaultHeadImageDirect + stuId + ".jpg");
                if (defaultFile.exists()) {
                    //从本地获取默认头像
                    bytes = FileUtil.fileToBytes(defaultFile);
                } else {
                    //从教务系统获取默认头像并保存本地
                    bytes = jwService.getXszp(request, cookie);
                    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                    BufferedImage bil1 = ImageIO.read(bais);
                    ImageIO.write(bil1, "jpg", defaultFile);
                }
            }
            return ReturnData.success(Base64.getEncoder().encodeToString(bytes));
        } else {
            // 如果没设置过头像则使用默认头像
            File file = new File(defaultHeadImageDirect + stuId + ".jpg");
            byte[] bytes;
            if (file.exists()) {
                //从本地获取默认头像
                bytes = FileUtil.fileToBytes(file);
            } else {
                //从教务系统获取默认头像
                bytes = jwService.getXszp(request, cookie);
                File defaultFile = new File(defaultHeadImageDirect + stuId + ".jpg");
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                BufferedImage bil1 = ImageIO.read(bais);
                ImageIO.write(bil1, "jpg", defaultFile);
            }
            return ReturnData.success(Base64.getEncoder().encodeToString(bytes));
        }
    }
    static void ByteToFile(byte[] bytes)throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedImage bi1 =ImageIO.read(bais);
        try {
            File w2 = new File("C:\\Users\\zsw\\Desktop\\1.jpg");//可以是jpg,png,gif格式
            ImageIO.write(bi1, "jpg", w2);//不管输出什么格式图片，此处不需改动
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            bais.close();
        }
    }

}
