package org.example.webtest.Service;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Primary
public class SecurityServiceWithDoubao implements ISecurityService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceWithDoubao.class);

    final String API_KEY = "8cbbe228-d6f2-407f-a14a-4fdddebb54d4";
    final String VISION_MODEL_URL = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
    final String VISION_MODEL_ID = "doubao-1-5-vision-pro-32k-250115";
    @Autowired
    private PromptService promptService;

    @Override
    public String getNotice(String imageUrl, String sceneType) {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();

        ArkService service = ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool).baseUrl(VISION_MODEL_URL).apiKey(API_KEY).build();
        final List<ChatMessage> messages = new ArrayList<>();
        final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
        multiParts.add(ChatCompletionContentPart.builder().type("text").text(
                promptService.getPrompt(sceneType)
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
                .model(VISION_MODEL_ID)
                .messages(messages)
                .build();
        List<String> output = new ArrayList<>();
        try {
            service.createChatCompletion(chatCompletionRequest).getChoices().forEach(choice -> output.add((String)choice.getMessage().getContent()));
        } catch (Exception e) {
            logger.error("create chat completion error. imageUrl:"+imageUrl+"  "+e.getMessage());
        } finally {
            service.shutdownExecutor();
        }

        return outputFilter(output.toString());
    }

    private String outputFilter(String output) {
        return output.replaceAll("\\s","");
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
