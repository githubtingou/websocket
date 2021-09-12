package com.ting.websocket.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * webSocket服务器
 *
 * @author ting
 * @version 1.0
 * @date 2021/9/11
 */
// 地址前面要加 "/"
@ServerEndpoint(value = "/websocket/{userId}")
@Slf4j
@Component
public class WebsocketServer {

    /**
     * 连接总数
     */
    private static final AtomicInteger count = new AtomicInteger(0);

    /**
     * client集合
     */
    private static final Map<String, WebsocketServer> clientMap = new ConcurrentHashMap<>();
    private Session session;
    private String userId;


    /**
     * 连接
     *
     * @param userId  用户标识
     * @param session session
     */
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session session) {
        log.info("{}连接服务器", userId);

        this.session = session;
        this.userId = userId;
        if (clientMap.containsKey(userId)) {
            clientMap.remove(userId);
            clientMap.put(userId, this);
            return;
        }
        clientMap.put(userId, this);
        count.incrementAndGet();

    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        log.info("{}关闭服务器连接", userId);
        clientMap.remove(userId);
        count.decrementAndGet();
    }

    /**
     * 发送数据
     *
     * @param message {@link WebsocketMsg}数据
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("发送数据:{}", message);
        WebsocketMsg websocketMsg = JSON.parseObject(message, WebsocketMsg.class);
        sendMsg(websocketMsg);

    }

    /**
     * 消息发送
     *
     * @param websocketMsg {@link WebsocketMsg}
     */
    private void sendMsg(WebsocketMsg websocketMsg) {

        // 群发
        if (websocketMsg.isSendAll()) {
            clientMap.forEach((userId, websocket) -> {

                // 是否给发送者发送消息
                if (websocketMsg.isSendMe()) {
                    websocket.session.getAsyncRemote().sendText(websocketMsg.getMsg());
                    return;
                }
                if (!this.userId.equals(userId)) {
                    websocket.session.getAsyncRemote().sendText(websocketMsg.getMsg());
                }

            });
            return;

        }
        // 定向发送
        websocketMsg.getUserIdList().forEach(userId -> {

            if (websocketMsg.isSendMe() && clientMap.containsKey(userId)) {
                WebsocketServer websocket = clientMap.get(userId);
                websocket.session.getAsyncRemote().sendText(websocketMsg.getMsg());
                return;
            }

            if (clientMap.containsKey(userId) && !this.userId.equals(userId)) {
                WebsocketServer websocket = clientMap.get(userId);
                websocket.session.getAsyncRemote().sendText(websocketMsg.getMsg());

            }
        });

    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
    }

    /**
     * 获取连接总数
     *
     * @return int
     */
    public int getClientSize() {
        return count.get();
    }

    /**
     * 获取所有客户端名称
     *
     * @return 客户端名称集合
     */
    public Set<String> getAllClient() {
        return clientMap.keySet();
    }

    /**
     * 根据用户id获取session
     *
     * @param userId
     * @return
     */
    public Session getSessionByUserId(String userId) {
        if (clientMap.containsKey(userId)) {
            WebsocketServer websocketServer = clientMap.get(userId);
            return websocketServer.session;
        }
        return null;

    }

    public void sendMsgByUserId(WebsocketMsg websocketMsg) {
        if (clientMap.containsKey(websocketMsg.getSender())) {
            WebsocketServer websocketServer = clientMap.get(websocketMsg.getSender());
            websocketServer.sendMsg(websocketMsg);
            return;
        }
        log.warn("没有该客户端");
    }

}
