package org.example.webtest.Service.impl;

import org.example.webtest.Model.SecurityInfo;
import org.example.webtest.Service.ISecurityService;
import org.example.webtest.Utils.PromptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SecurityServiceWithBaidu implements ISecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceWithBaidu.class);
    @Autowired
    private PromptService promptService;
    public SecurityInfo getNotice(String imageUrl, String fileName, String sceneType) {
        //return "{\"result\":[\"0\"],\"result_num\":1}";
        return new SecurityInfo();
    }

    public SecurityInfo getNotice(MultipartFile file, String fileName, String sceneType) {
        //return "{\"result\":[\"0\"],\"result_num\":1}";
        return new SecurityInfo();
    }
}
