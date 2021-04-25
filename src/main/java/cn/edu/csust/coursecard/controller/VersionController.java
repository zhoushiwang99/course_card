package cn.edu.csust.coursecard.controller;

import cn.edu.csust.coursecard.bean.Version;
import cn.edu.csust.coursecard.common.CodeEnum;
import cn.edu.csust.coursecard.common.ReturnData;
import cn.edu.csust.coursecard.dao.VersionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsw
 * @date 2020/02/16 23:02
 */
@RestController
public class VersionController {

    @Autowired
    VersionDAO versionDAO;

    @Value("${my.host}")
    String host;

    @Value("${server.port}")
    String port;

    @GetMapping("/getLastVersion")
    public ReturnData getLastVersion() {
        Version lastVersion = versionDAO.getLastVersion();
        if (lastVersion == null) {
            return ReturnData.fail(CodeEnum.SYSTEM_ERROR.getCode(), "暂无apk下载");
        } else {
//            lastVersion.setApkPath("http://47.106.159.165:8081/apk/" + lastVersion.getApkPath());
            lastVersion.setApkPath("http://" + host + ":" + port + "/apk/" + lastVersion.getApkPath());
            return ReturnData.success(lastVersion);
        }


    }

}
