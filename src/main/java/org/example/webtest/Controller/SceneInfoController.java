package org.example.webtest.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class SceneInfoController {
    final HashMap<String, String> sceneType = new HashMap<String, String>()  {{
        put("0", "安全生产");
        put("1", "交通管理");
        put("2", "市容市政");
        put("3", "自然灾害");
        put("4", "水文气象");
    }};
    @GetMapping("/scene_type")
    public APIResponsePacker<HashMap<String, String>> getSceneType() {
        return new APIResponsePacker<HashMap<String, String>>(0,"", sceneType);
    }
}
