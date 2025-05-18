package org.example.webtest.Service;

import org.example.webtest.Model.SecurityInfo;
import org.springframework.web.multipart.MultipartFile;


public interface ISecurityService {
    SecurityInfo getNotice(String imageUrl, String fileName, String sceneType);
    SecurityInfo getNotice(MultipartFile file, String fileName, String sceneType);
}
