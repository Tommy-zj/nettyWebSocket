package cn.netty.nettywebsocket.netty;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO 鉴权
 * @date 2022/9/21 14:19:10
 */

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest msg1 = (FullHttpRequest) msg;
            //根据请求头的 auth-token 进行鉴权操作
            String authToken = msg1.headers().get("auth-token");
            System.out.println(">>>>>>>>>>>>鉴权操作>>>" + authToken);
            if (false) {
                refuseChannel(ctx);
                return;
            }
            //鉴权成功，添加channel用户组
            MyChannelHandlerPool.channelGroup.add(ctx.channel());
        }
        ctx.fireChannelRead(msg);
    }

    private void refuseChannel(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
        ctx.channel().close();
    }
}

