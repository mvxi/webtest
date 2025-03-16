package org.example.webtest.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceWithBaidu implements ISecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceWithBaidu.class);
    @Autowired
    private PromptService promptService;
    public String getNotice(String imageUrl, String sceneType) {
        return "{\"result\":[\"0\"],\"result_num\":1}";
    }
}
