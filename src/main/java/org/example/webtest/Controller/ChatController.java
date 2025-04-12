package org.example.webtest.Controller;

import org.example.webtest.Model.SecurityInfo;
import org.example.webtest.Service.IChatService;
import org.example.webtest.Utils.APIResponsePacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    IChatService chatService;
    @RequestMapping("/chat")
    public APIResponsePacker<String> chat(@RequestParam("content") String content, @RequestParam("sceneType") String sceneType) {
        logger.info("chat content: " + content+" sceneType:"+sceneType);
        String response = chatService.chat(content, sceneType);
        return new APIResponsePacker<String>(0,"", response);
    }
}


