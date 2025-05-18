package org.example.webtest.Utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PromptService {
    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);
    private static String promptFile = "./prompt_list.txt";
    private static String securityPrompt = "###背景：在施工现场进行安全巡视，查找施工的过程中不符合安全法规的地方，来进行整改\n" +
            "###角色：你是安全施工巡检员 ###要求：指出不符合的安全法规的项，要求两个版本，一个重要版，一个详细版\n"+
            "###输出内容：关注4个方面，安全隐患、重要提醒、法律依据、巡检建议，对每个方面展开多个维度的描述，并对每个维度有简短概括。针对以上内容，生成一个重要版(important_version)，一个详细版(detailed_version)，以及安全隐患总数量，最后对所有输出内容以json格式输出,参考输出格式如下："+
            "{" +
            "\"risk_count\": 3," +
            "\"important_version\": " +
            "{\"安全隐患\": \"1. 挖掘机作业区域与工人作业区域重合，易引发机械伤害；2. 部分工人 安全帽佩戴不规范，防护不足。\","+
            "\"重要提醒\": \"挖掘机与工人近距离交叉作业，可能导致严重伤亡事故。\"," +
            "\"法律依据\":\"《中华人民共和国安全生产法》第四十二条规定，生产经营单位应当为从业人员提供符合国家标准或者行业标准的劳动防护用品，并监督、教育从业人员按照使用规则佩戴、使用。\""+
            "\"巡检建议\":\"1.划分挖掘机和工人的作业区域，设置警示标识；2.加强对工人安全帽佩戴规范的教育和监督。\" }," +
            "\"detailed_version\": {"+
            "\"安全隐患\": {"+
            "\"机械伤害风险\": \"挖掘机在作业过程中，其活动范围大、操作具有一定的不可预测性，当前与 工人作业区域重合，一旦挖掘机操作失误或失控，极有可能碰撞、挤压到附近工人，造成严重的身体伤害。\"},"+
            "\"重要提醒\": {"+
            "\"交叉作业风险\": \"挖掘机与工人近距离交叉作业，由于机械的力量巨大，一旦发生意外，可能 造成群死群伤的严重后果，对工人生命安全构成极大威胁，严重影响工程进度和社会稳定。\"}," +
            "\"法律依据\": {" +
            "\"劳动防护用品规定\": \"《中华人民共和国安全生产法》第四十二条明确指出，生产经营单位有责任为从业人员提供符合标准的劳动防护用品，并对其佩戴和使用进行监督与教育，以保障从业人员在作业过程中的安全。\" },"+
            "\"巡检建议\": {" +
            "\"安全帽规范管理\": \"组织工人进行安全帽佩戴规范的专项培训，提高工人安全意识，同时加强日常监督检查，对不规范佩戴行为及时纠正和处理。\" },"+
            "}," +
            "}";

    private static String chatPrompt = "###角色：你是安全法律法规专家，对用户的问题给与相关方面的专业解答，字数限定在200以内\n" +
            "###问题：";




    public static void updatePromptFromFile() {
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

    public static String getPrompt(String sceneType) {
        String prompt = "";
        switch (sceneType) {
            case "10000":
                updatePromptFromFile();
                prompt = securityPrompt;
                break;
            case "20000":
                prompt = chatPrompt;
                break;
            default:
                prompt = "###背景：图片里有没有违规行为，从明确违规以及可能违规两个方面描述";
        }
        return prompt;
    }

    public static String getChatPrompt(String content, String sceneType) {
        return getPrompt(sceneType)+content;
    }
}
