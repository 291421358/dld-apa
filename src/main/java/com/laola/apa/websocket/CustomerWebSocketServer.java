package com.laola.apa.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * websocket服务器
 *
 * @Description:
 * @Author: T
 * @Date: 2021/7/13 0:46
 * @Version 1.0
 **/
public class CustomerWebSocketServer extends WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerWebSocketServer.class);

    private WebSocket webSocket;

    public CustomerWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // 点对点发送消息
        conn.send("Welcome to the server!");
        this.setWebSocket(conn);
        // 广播消息
        broadcast( "new connection: " + handshake.getResourceDescriptor());
        LOGGER.info("与客户端{}建立了连接",   conn.getLocalSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        LOGGER.info("客户端{}断开连接，错误码={}，原因：{}", conn.getRemoteSocketAddress(), code, reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        LOGGER.info("从客户端{}接收到消息{}",conn.getRemoteSocketAddress(), message);
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        LOGGER.info("从客户端{}接收到消息{}", conn.getRemoteSocketAddress(), message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        LOGGER.error("socket连接错误：{}", conn.getRemoteSocketAddress(), ex.getMessage());
    }

    @Override
    public void onStart() {
        LOGGER.info("成功开启socket服务端！");
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8887;

        WebSocketServer server = new CustomerWebSocketServer(new InetSocketAddress(host, port));
        server.run();
    }

}
