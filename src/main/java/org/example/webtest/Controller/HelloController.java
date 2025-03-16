package org.example.webtest.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        System.out.println("hello mmmmm");
        return "Hello world111";
    }
}


