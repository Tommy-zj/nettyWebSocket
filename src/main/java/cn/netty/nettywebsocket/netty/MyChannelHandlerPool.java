package cn.netty.nettywebsocket.netty;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO
 * @date 2022/9/21 11:54:38
 */

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MyChannelHandlerPool
 * 通道组池，管理所有websocket连接
 *
 * @author JJ
 */
public class MyChannelHandlerPool {

    public MyChannelHandlerPool() {
    }

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 用户id=>channel
     **/
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();
    /**
     * 房间
     **/
    public static Map<String, Map<String, Channel>> roomMap = new ConcurrentHashMap<>();

}

