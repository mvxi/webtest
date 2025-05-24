package org.example.webtest.Service.impl;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.example.webtest.Model.SecurityInfo;
import org.example.webtest.Service.ISecurityService;
import org.example.webtest.Utils.Constants;
import org.example.webtest.Utils.JsonEscape;
import org.example.webtest.Utils.PromptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Primary
public class SecurityServiceWithDoubao implements ISecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceWithDoubao.class);


    @Autowired
    private PromptService promptService;

    @Override
    public SecurityInfo getNotice(String imageUrl, String fileName, String sceneType) {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(Constants.VISION_MODEL_URL).apiKey(Constants.API_KEY).build();

        final List<ChatMessage> messages = new ArrayList<>();
        final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
        multiParts.add(ChatCompletionContentPart.builder().type("text").text(
                PromptService.getPrompt(sceneType)
        ).build());
        multiParts.add(ChatCompletionContentPart.builder().type("image_url").imageUrl(
                new ChatCompletionContentPart.ChatCompletionContentPartImageURL(
                        imageUrl
                )
        ).build());
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER)
                .multiContent(multiParts).build();
        final ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT)
                        .content("{").build();
        messages.add(userMessage);
        messages.add(assistantMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.VISION_MODEL_ID)
                .messages(messages)
                .build();
        List<String> output = new ArrayList<>();
        try {
            service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> output.add((String)choice.getMessage().getContent()));

        } catch (Exception e) {
            logger.error("create vision completion error by imageurl. imageUrl:"+imageUrl+"  .error info:"+e.getMessage());
        } finally {
            service.shutdownExecutor();
        }

        SecurityInfo securityInfo = new SecurityInfo();
        if(!output.isEmpty()){
            securityInfo = outputFormat(output.get(0));
        }
        return securityInfo;
    }




    private SecurityInfo outputFormat(String output) {
        //1. 删除空格，\n等字符
        String filter = output.replaceAll("\\s","");
        //2. 如果字符串不是以 {}开头结尾，手动拼一个。。。
        String tempStr = "{" + filter +"}";

        JsonEscape jsonEscape = new JsonEscape();
        return jsonEscape.unSecurityString(tempStr);
       // return JsonEscape.unSecurityString(tempStr);
    }

    public SecurityInfo getNotice(MultipartFile file, String fileName, String sceneType) {
        String fileContent = convertMultipartFileToBase64(file);
        logger.info("by content 1"+fileContent.length());

        if (fileContent == null || fileContent.isEmpty()) {
            return null;
        }
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(Constants.VISION_MODEL_URL).apiKey(Constants.API_KEY).build();

        final List<ChatMessage> messages = new ArrayList<>();
        final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
        logger.info("by content 2"+fileContent.length());

        multiParts.add(ChatCompletionContentPart.builder().type("text").text(
                PromptService.getPrompt(sceneType)
        ).build());
        multiParts.add(ChatCompletionContentPart.builder().type("image_url").imageUrl(
            new ChatCompletionContentPart.ChatCompletionContentPartImageURL(
                    fileContent
            )
        ).build());        
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER)
                .multiContent(multiParts).build();
        final ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT)
                .content("{").build();
        messages.add(userMessage);
        messages.add(assistantMessage);
        logger.info("by content 3"+fileContent.length());

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.VISION_MODEL_ID)
                .messages(messages)
                .build();
        List<String> output = new ArrayList<>();
        try {
            service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> output.add((String)choice.getMessage().getContent()));
        } catch (Exception e) {
            logger.error("create vision completion by content error. filename:"+fileName+" . error info:"+e.getMessage());
        } finally {
            service.shutdownExecutor();
        }
        logger.info("by content 4");
        return outputFormat(output.get(0));
    }

    public String convertMultipartFileToBase64(MultipartFile file)  {
        byte[] fileContent;
        try {
            fileContent = file.getBytes(); // 获取文件内容
        } catch (IOException e) {
            logger.error("file "+file.getOriginalFilename()+"  getbyte error. info:"+e.getMessage());
            return "";
        }
        
        // 获取文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            logger.error("Invalid file type: " + contentType);
            return "";
        }
        
        // 转换为Base64字符串并添加data URI前缀
        String base64Content = Base64.getEncoder().encodeToString(fileContent);
        return "data:" + contentType + ";base64," + base64Content;
    }
}

/**
 ###背景：在施工现场进行安全巡视，查找施工的过程中不符合安全法规的地方，来进行整改
 ###角色：你是安全施工巡检员
 ###要求：指出不符合的安全法规的项，要求两个版本，一个重要版，一个详细版\n" +
 "###输出内容包括以下4个方面，对每个方面展开多个维度的描述，并对每个维度有简短概括，最后把两个版本整体以json格式输出\n\n" +
 "1 潜在风险或安全隐患\n" +
 "2 重大事故隐患提醒\n" +
 "3 法律依据\n" +
 "4 整改建议";

 */
