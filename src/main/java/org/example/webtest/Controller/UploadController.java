package org.example.webtest.Controller;

import org.example.webtest.Exception.BusinessException;
import org.example.webtest.Model.SecurityInfo;
import org.example.webtest.Service.ISecurityService;
import org.example.webtest.Utils.APIResponsePacker;
import org.example.webtest.Utils.Constants;
import org.example.webtest.Utils.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg","image/jpg","image/png","image/gif","image/bmp","image/webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Autowired
    ISecurityService securityService;

    @PostMapping("/upload")
    public APIResponsePacker<SecurityInfo> upload(@RequestParam("imgFile") MultipartFile file, 
                                                 @RequestParam("imgName") String name,
                                                 @RequestParam(value = "sceneType", required = false) String sceneType,
                                                 @RequestParam(value = "imgMd5Name",required = false, defaultValue = "") String imgMd5Name,
                                                 @RequestParam(value = "lat",required = false, defaultValue = "0") String lat,
                                                 @RequestParam(value = "lng",required = false, defaultValue = "0") String lng,
                                                 @RequestParam(value = "androidId", required = false, defaultValue = "0") String did) {
        long startTime = System.currentTimeMillis();
        try {
            checkImageInfo(file);
            String fileName = formatFileName(file.getOriginalFilename(), imgMd5Name);
            storeImage(file, fileName);

            String fileUrl = Constants.DOMAIN_IMAGE + fileName;
            logger.info("upload file url:" + fileUrl+"  scene type:" + sceneType+"  md5name:" + imgMd5Name +"   original name:"+file.getOriginalFilename()+ " lat:" + lat + " lng:" + lng+ " androidid:" + did);

            SecurityInfo security = securityService.getNotice(fileUrl, fileName, sceneType);
            return new APIResponsePacker<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), security);
        } finally {
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            logger.info("Upload execution time: {} ms for file: {}", executionTime, file.getOriginalFilename());
        }
    }

    private void checkImageInfo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_NOT_FOUND, "File is empty");
        }

        String contentType = file.getContentType();
        if (!ALLOW_TYPES.contains(contentType)) {
            throw new BusinessException(ResultCode.FILE_TYPE_ERROR, "Invalid file type: " + contentType);
        }

        long fileSize = file.getSize();
        if (fileSize > MAX_FILE_SIZE) {
            throw new BusinessException(ResultCode.FILE_SIZE_ERROR, "File size exceeds limit: " + fileSize);
        }
    }

    private String formatFileName(String originalFilename, String md5FileName) {
        String fileName = "";
        if (md5FileName != null && !md5FileName.isEmpty()) {
            fileName = md5FileName;
        }
        
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        if (!extension.isEmpty() && !fileName.endsWith(extension)) {
            fileName += extension;
        }
        
        return fileName;
    }

    private void storeImage(MultipartFile file, String fileName) {
        File dir = new File("uploadFile");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = dir.getAbsolutePath() + File.separator + fileName;
        try {
            file.transferTo(new File(filePath));
        } catch(IOException e) {
            logger.error("store image "+fileName+" error. info:"+e.getMessage());
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR, "Failed to store file: " + e.getMessage());
        }
    }

    @RequestMapping("llm")
    public APIResponsePacker<SecurityInfo> llm() {
        logger.info("llm start:");
        String fileName = "fbc6b16e9db7d59b8b09698c7ede0f7c.jpeg";
        String imageUrl = "https://www.xwhr8.com/image/fbc6b16e9db7d59b8b09698c7ede0f7c.jpeg";
        return new APIResponsePacker<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), 
            securityService.getNotice(imageUrl, fileName, "10000"));
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
