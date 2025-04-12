package org.example.webtest.Controller;

import org.example.webtest.Model.SecurityInfo;
import org.example.webtest.Service.ISecurityService;
import org.example.webtest.Utils.APIResponsePacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@RestController
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    ISecurityService securityService;
    @PostMapping("/upload")
    public APIResponsePacker<SecurityInfo> upload(@RequestParam("imgFile") MultipartFile file, @RequestParam("imgName") String name,
                                                  @RequestParam(value = "sceneType", required = false) String sceneType,
                                                  @RequestParam(value = "imgMd5Name",required = false, defaultValue = "") String imgMd5Name,
                                                  @RequestParam(value = "lat",required = false, defaultValue = "0") String lat,
                                                  @RequestParam(value = "lng",required = false, defaultValue = "0") String lng,
                                                  @RequestParam(value = "androidId", required = false, defaultValue = "0") String did

    ) throws Exception {
        // 设置上传至项目文件夹下的uploadFile文件夹中，没有文件夹则创建
        File dir = new File("uploadFile");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
        String fileUrl = "https://www.xwhr8.com/image/" + file.getOriginalFilename();
        logger.info("upload file url:" + fileUrl+"  scene type:" + sceneType+"  md5name:" + imgMd5Name + " lat:" + lat + " lng:" + lng+ " androidid:" + did);

        file.transferTo(new File(filePath));
        //file.transferTo(new File(dir.getAbsolutePath() + File.separator + name + ".png"));

        //return "上传完成！文件名：" + fileUrl + " \n ";
        SecurityInfo security = securityService.getNotice(fileUrl, sceneType);
        return new APIResponsePacker<SecurityInfo>(0,"", security);
    }

    @RequestMapping("llm")
    public  APIResponsePacker<SecurityInfo> llm()  {
        logger.info("llm:");

        String imageUrl =   "https://www.xwhr8.com/image/fbc6b16e9db7d59b8b09698c7ede0f7c.jpeg";
        return new APIResponsePacker<SecurityInfo>(0,"", securityService.getNotice(imageUrl,"10000"));
    }
}
/*
仅从这张建筑施工场景图片本身难以确切判定是否存在违规行为。以下从可能的方面分析：
### 从人员防护角度
图片中工人们佩戴了安全帽，在个人基础防护装备方面符合基本安全要求，但仅安全帽这一项不能完全代表整体防护合规，还需看是否有其他防护用品如安全鞋、手套等（图片中部分人员戴了手套 ），不过仅据此不能判定存在违规。
### 从设备操作和场地角度
挖掘机等设备正常作业，场地有一定的杂乱但整体是常见的施工现场状态，未明显看到设备违规操作（如设备带病作业、超范围作业等）或场地存在明显安全隐患（如未设置警示标识等，但图片视角有限） 。

若要准确判断是否存在违规行为，还需要更多详细信息，比如是否有相关许可证、是否存在安全管理漏洞等。
 */
