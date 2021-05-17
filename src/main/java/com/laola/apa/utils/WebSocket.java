package com.laola.apa.utils;

import com.laola.apa.server.OnMessageServer;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;

/**
 */
//websocket的地址。前端连接时，
//地址为ws://'+host+'/webSocket/user1
@ServerEndpoint("/webSocket/{username}")
//使websock在spring boot中生效，交由spring 管理
@Component

public class WebSocket {
    //在线的客户端数量
    private static int onlineCount = 0;
    //客户端集合，连接时放入，key为username
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();
    private Session session;
    //客户端用户名，连接时赋值
    private String username;
    //存储连接用户的session
    private static CopyOnWriteArraySet<Session> connections = new CopyOnWriteArraySet<>();
    Logger logger = Logger.getGlobal();
    /**
     * 打开websocket连接
     *
     * @param username
     * @param session
     * @throws IOException
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) throws IOException {

        this.username = username;
        this.session = session;
        // 添加一个在线客户端数量
        addOnlineCount();
        //将MyWebSocket对象置入clients
        clients.put(username, this);
        connections.add(session);
        System.out.println("connecting");
    }

    /**
     * 关闭websocket连接
     *
     * @throws IOException
     */
    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    /**
     * 接收消息
     * 处理业务
     * 在此处与串口发生通讯
     *
     * @param message uuuuuu    * @throws IOException MyWebSocket
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        OnMessageServer onMessageServer = new OnMessageServer();
        try {
            onMessageServer.onMessage(username, message ,this);
        } catch (IOException e) {
            sendMessageTo("701", username);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("OnError");
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message
     * @param To
     * @throws IOException
     */
    public void sendMessageTo(String message, String To) throws IOException {
        //关于getBasicRemote 与getAsyncRemote 的区别，在
        /** @throws MyWebSocket **/ //中有
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (WebSocket item : clients.values()) {
            if (item.username.equals(To))
                //getAsyncRemote 是异步请求
                item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 发送全体消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessageAll(String message) throws IOException {
        //遍历clients
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * 获取所有在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数自加
     */
    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    /**
     * 在线人数自减
     */
    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    /**
     * 获取所有客户端信息
     *
     * @return
     */
    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }
}