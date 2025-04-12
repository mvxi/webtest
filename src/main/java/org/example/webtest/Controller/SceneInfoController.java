package org.example.webtest.Controller;

import org.example.webtest.Utils.APIResponsePacker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class SceneInfoController {
    final HashMap<String, String> sceneType = new HashMap<String, String>()  {{
        put("10000", "安全生产");
        put("10100", "安全城市");
        put("10200", "安全交通");
    }};
    @GetMapping("/scene_type")
    public APIResponsePacker<HashMap<String, String>> getSceneType() {
        return new APIResponsePacker<HashMap<String, String>>(0,"", sceneType);
    }
}
