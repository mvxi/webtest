package org.example.webtest.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.webtest.Model.SecurityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JsonEscape {
    private static final Logger logger = LoggerFactory.getLogger(JsonEscape.class);

    @Autowired
    private  ObjectMapper objectMapper = new ObjectMapper();

    public SecurityInfo unSecurityString(String jsonString) {
        logger.info("llm output json:" + jsonString);
        SecurityInfo securityInfo = new SecurityInfo();
        try {
            securityInfo = objectMapper.readValue(jsonString, SecurityInfo.class);
        } catch (Exception e) {
            logger.error("transform json to POJO error. info:"+e.getMessage());
        }
        return securityInfo;
    }
}
