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
     * @apiNote 打开websocket连接
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param username
     * @param session
     * @return
     **/
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
     * @apiNote 关闭websocket连接
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param
     * @return
     **/
    @OnClose
    public void onClose() throws IOException {
        clients.remove(username);
        subOnlineCount();
    }

    /**
     * @apiNote 接收websocket消息
     *  处理业务
     *  在此处与串口发生通讯
     * @author tzhh
     * @date 2021/5/27 17:01
     * @param message
     * @return
     **/
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
     * @apiNote 通过websocket发送消息
     * @author tzhh
     * @date 2021/5/27 17:00
     * @param message
	 * @param To
     * @return
     **/
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
     * @apiNote 发送全体消息
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param message
     * @return
     **/
    public void sendMessageAll(String message) throws IOException {
        //遍历clients
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    /**
     * @apiNote 获取所有在线人数
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param
     * @return {@link int}
     **/
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * @apiNote 在线人数自加
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param
     * @return
     **/
    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    /**
     * @apiNote 在线人数自减
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param
     * @return
     **/
    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    /**
     * @apiNote 获取所有客户端信息
     * @author tzhh
     * @date 2021/5/27 17:02
     * @param
     * @return {@link Map< String, WebSocket>}
     **/
    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }
}