package cn.netty.nettywebsocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO
 * @date 2022/9/20 13:42:55
 */
@RestController
@RequestMapping("/test")
public class HelloController {
    @GetMapping("hello")
    public String hello() {
        return "hello word!";
    }
}
