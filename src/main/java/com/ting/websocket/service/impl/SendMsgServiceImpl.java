package com.ting.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.ting.websocket.config.WebsocketMsg;
import com.ting.websocket.config.WebsocketServer;
import com.ting.websocket.service.ISendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 消息发送实现接口
 *
 * @author ting
 * @version 1.0
 * @date 2021/9/12
 */
@Service
@Slf4j
public class SendMsgServiceImpl implements ISendMsgService {

    private final WebsocketServer websocketServer;

    public SendMsgServiceImpl(WebsocketServer websocketServer) {
        this.websocketServer = websocketServer;
    }

    @Override
    public void sendMsg(WebsocketMsg websocketMsg) {
        log.info("消息发送");
        websocketServer.sendMsgByUserId(websocketMsg);
    }

    @Override
    public int getClients() {
        return websocketServer.getClientSize();
    }

    @Override
    public Set<String> getAllClient() {
        return websocketServer.getAllClient();
    }
}
