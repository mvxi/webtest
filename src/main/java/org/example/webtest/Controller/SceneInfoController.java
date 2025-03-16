package org.example.webtest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class SceneInfoController {
    final HashMap<String, String> sceneType = new HashMap<String, String>()  {{
        put("10000", "安全生产");
        put("10100", "交通管理");
        put("10200", "市容市政");
        put("10300", "自然灾害");
        put("10400", "水文气象");
    }};
    @GetMapping("/scene_type")
    public APIResponsePacker<HashMap<String, String>> getSceneType() {
        return new APIResponsePacker<HashMap<String, String>>(0,"", sceneType);
    }
}
