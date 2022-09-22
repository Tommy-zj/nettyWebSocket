package cn.netty.nettywebsocket.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO 开启WebSocket功能
 * @date 2022/9/20 10:37:50
 */
@Configuration
@Slf4j
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("====>>>>底层基于netty的websocket");
        return new ServerEndpointExporter();
    }
}