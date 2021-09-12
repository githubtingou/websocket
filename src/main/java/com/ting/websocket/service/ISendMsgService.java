package com.ting.websocket.service;

import com.ting.websocket.config.WebsocketMsg;

import java.util.Set;

/**
 * 消息发送
 *
 * @author ting
 * @version 1.0
 * @date 2021/9/12
 */
public interface ISendMsgService {

    /**
     * 发送消息
     *
     * @param websocketMsg {@link WebsocketMsg}
     */
    void sendMsg(WebsocketMsg websocketMsg);

    /**
     * 获取了连接的数量
     *
     * @return 数量
     */
    int getClients();

    /**
     * 获取所有用户
     *
     * @return 数据集合
     */
    Set<String> getAllClient();


}
