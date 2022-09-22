package cn.netty.nettywebsocket;

import cn.netty.nettywebsocket.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author JJ
 */
@SpringBootApplication(scanBasePackages = {"cn.netty.nettywebsocket.*"})
public class NettyWebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyWebSocketApplication.class, args);

        try {
            new NettyServer(8082).start();
        }catch(Exception e) {
            System.out.println("NettyServerError:"+e.getMessage());
        }
    }

}
