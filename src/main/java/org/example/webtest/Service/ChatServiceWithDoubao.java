package org.example.webtest.Service;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class ChatServiceWithDoubao implements IChatService {
    private static final Logger logger = LoggerFactory.getLogger(ChatServiceWithDoubao.class);

    final String API_KEY = "8cbbe228-d6f2-407f-a14a-4fdddebb54d4";
    final String CHAT_MODEL_ID = "doubao-1-5-lite-32k-250115";

    @Autowired
    private PromptService promptService;

    @Override
    public String chat(String content, String sceneType) {
        String result = "";
        // 创建ArkService实例
        ArkService arkService = ArkService.builder().apiKey(API_KEY).build();

        // 初始化消息列表
        List<ChatMessage> chatMessages = new ArrayList<>();

        // 创建用户消息
        String prompt = promptService.getChatPrompt(content, sceneType);
        ChatMessage userMessage = ChatMessage.builder()
                .role(ChatMessageRole.USER) // 设置消息角色为用户
                .content(prompt) // 设置消息内容
                .build();

        // 将用户消息添加到消息列表
        chatMessages.add(userMessage);

        // 创建聊天完成请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(CHAT_MODEL_ID)// 需要替换为Model ID
                .messages(chatMessages) // 设置消息列表
                .build();
        List<String> output = new ArrayList<>();
        // 发送聊天完成请求并打印响应
        try {
            // 获取响应并打印每个选择的消息内容
            arkService.createChatCompletion(chatCompletionRequest)
                    .getChoices()
                    .forEach(choice ->output.add((String)choice.getMessage().getContent()));
            result = output.get(0);
        } catch (Exception e) {
            logger.error("create chat completion error. content:"+content+"  "+e.getMessage());
        } finally {
            // 关闭服务执行器
            arkService.shutdownExecutor();
        }
        logger.info("chat prompt:"+prompt+"  result:"+result);
        return result;
    }
}
