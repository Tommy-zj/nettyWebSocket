package cn.netty.nettywebsocket.websocket;

import io.netty.handler.codec.http.HttpHeaders;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tommy-zj
 * @version 1.0
 * @description: TODO springboot-netty 实现群聊
 * @date 2022/9/20 10:40:55
 */
@ServerEndpoint(port = "${ws.port}", host = "${ws.host}", path = "/websocket/{name}/{room}")
@Component
public class MyWebSocket {

    private String message;

    private HashMap statusMap = new HashMap();

    /**
     * 全部连接
     **/
    public static Map<String, Session> count = new ConcurrentHashMap<>();

    /**
     * 全部房间
     **/
    public static Map<String, Map<String, Session>> rooms = new ConcurrentHashMap<>();

    private String name;

    private String room;


    public static void JoinRoom(String roomName, String name, Session session) {
        count.put(name, session);
        if (!rooms.containsKey(roomName)) {
            /**
             房间不存在，新建房间
             **/
            Map<String, Session> room = new HashMap();
            room.put(name, session);
            rooms.put(roomName, room);
            return;
        }
        /**
         房间存在
         **/
        rooms.get(roomName).put(name, session);
        return;
    }


    // ws://127.0.0.1:8081/websocket/jj/12
    @OnOpen
    public void onOpen(Session session, HttpHeaders headers, @PathVariable String message,
                       @PathVariable String name, @PathVariable String room) throws IOException {
        //System.out.println("new connection");
        //System.out.println("房间号>>>" + room + "name>>>" + name);
        this.name = name;
        this.room = room;
        MyWebSocket.JoinRoom(room, name, session);
        this.statusMap.put("count", message);
        broadcast("欢迎: " + this.name + "加入了本聊天室" + ",共有: " + MyWebSocket.count.size() + "个人在线", room);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        //退出聊天室内
        count.remove(this.name);
        rooms.get(this.room).remove(this.name);
        System.out.println(this.name + ":离开了" + this.room + "聊天室内！");
    }


    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnMessage
    public void OnMessage(Session session, String message) {
        System.out.println(message);
        // System.out.println("房间号>>>" + this.room + "name>>>" + this.name);
        broadcast(this.name + ": " + message, this.room, session);
    }

    public void broadcast(String message, String roomName, Session session) {
        if (rooms.get(roomName) == null) {
            return;
        }
        for (Session session1 : rooms.get(roomName).values()) {
            if (session1 != session) {
                session1.sendText(message);
            }

        }
    }

    public void broadcast(String message, String roomName) {
        if (rooms.get(roomName) == null) {
            return;
        }
        for (Session session1 : rooms.get(roomName).values()) {
            session1.sendText(message);
        }
    }
}

