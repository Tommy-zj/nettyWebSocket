package cn.netty.nettywebsocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO ebSocket处理器，处理websocket连接相关
 * @date 2022/9/21 11:54:19
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private String name;
    private String room;
    private Channel channel;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端建立连接，通道开启！当前通道数量----" + MyChannelHandlerPool.channelGroup.size());
        //添加到channelGroup通道组
        // MyChannelHandlerPool.channelGroup.add(ctx.channel());

    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("与客户端断开连接，通道关闭！当前通道数量----" + MyChannelHandlerPool.channelGroup.size());
        //添加到channelGroup 通道组
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(this.name + ">来到了了>>>" + this.room);
        this.channel = ctx.channel();
        //首次连接是FullHttpRequest，处理参数 by zhengkai.blog.csdn.net
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            // System.out.println("uri-----" + uri);
            Map paramMap = getUrlParams(uri);
            this.name = String.valueOf(paramMap.get("name"));
            this.room = String.valueOf(paramMap.get("room"));
            addOrJoinRooms();
            online(ctx.channel(), paramMap.get("name") + "");
            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                // System.out.println("newUri----------" + newUri);
                request.setUri(newUri);
            }

        } else if (msg instanceof TextWebSocketFrame) {
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            broadcast(this.name + ": " + frame.text());
            System.out.println("客户端收到服务器数据：" + frame.text() + "------当前通道数量----" + MyChannelHandlerPool.channelGroup.size());
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }


    public void broadcast(String message) {
        for (Channel channel : MyChannelHandlerPool.roomMap.get(this.room).values()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    private void sendAllMessage(String message) {
        //收到信息后，群发给所有channel
        MyChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }


    private void addOrJoinRooms() {
        if (null != this.name && null != this.room) {
            if (MyChannelHandlerPool.roomMap.containsKey(this.room)) {
                // 房间存在
                MyChannelHandlerPool.roomMap.get(this.room).put(this.name, this.channel);
            } else {
                // 房间不存在
                Map<String, Channel> userChannelMap = new HashMap();
                userChannelMap.put(this.name, this.channel);
                MyChannelHandlerPool.roomMap.put(this.room, userChannelMap);
            }
        }
    }


    /**
     * 上线一个用户
     *
     * @param channel
     * @param userId
     */
    public void online(Channel channel, String userId) {
        //先判断用户是否在web系统中登录?
        //这部分代码个人实现,参考上面redis中的验证
        MyChannelHandlerPool.channelMap.put(userId, channel);
        AttributeKey<String> key = AttributeKey.valueOf("user");
        channel.attr(key).set(userId);
    }


    private static Map getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            return map;
        }
    }

}


