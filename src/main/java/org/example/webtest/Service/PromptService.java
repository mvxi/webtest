package org.example.webtest.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PromptService {
    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);
    final String promptFile = "./prompt_list.txt";
    private String securityPrompt = "###背景：在施工现场进行安全巡视，查找施工的过程中不符合安全法规的地方，来进行整改\n" +
            "###角色：你是安全施工巡检员 ###要求：指出不符合的安全法规的项，要求两个版本，一个重要版，一个详细版\n"+
            "###输出内容：关注4个方面，潜在风险或安全隐患、重大事故隐患提醒、法律依据、整改建议，对每个方面展开多个维度的描述，并对每个维度有简短概括。针对以上内容，生成一个重要版，一个详细版，最后对所有输出内容以json格式打包\n";


    public void updatePromptFromFile() {
        try {
            File file = new File(promptFile);
            String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
            if (!content.isEmpty()) {
                securityPrompt = content;
                logger.info("get prompt from file, prompt is :" + content);
            }
        } catch (IOException e) {
            logger.warn("get prompts from file,error info:" + e.toString());
        }
    }

    public String getPrompt(String sceneType) {
        String prompt = "";
        switch (sceneType) {
            case "0":
                updatePromptFromFile();
                prompt = securityPrompt;
                break;
            default:
                prompt = "###背景：图片里有没有违规行为，从明确违规以及可能违规两个方面描述";
        }
        return prompt;
    }

}
