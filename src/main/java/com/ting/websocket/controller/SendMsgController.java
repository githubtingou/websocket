package com.ting.websocket.controller;

import com.ting.websocket.config.WebsocketMsg;
import com.ting.websocket.service.ISendMsgService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 消息发送
 *
 * @author ting
 * @version 1.0
 * @date 2021/9/12
 */
@RestController
@RequestMapping(value = "/send")
public class SendMsgController {

    private final ISendMsgService iSendMsgService;

    public SendMsgController(ISendMsgService iSendMsgService) {
        this.iSendMsgService = iSendMsgService;
    }

    @PostMapping(value = "msg")
    public void sendMeg(@RequestBody WebsocketMsg websocketMsg) {
        iSendMsgService.sendMsg(websocketMsg);

    }

    @GetMapping(value = "getClientSize")
    public int getClientSize() {
        return iSendMsgService.getClients();
    }

    @GetMapping(value = "getAllClients")
    public Set<String> getAllClients() {
        return iSendMsgService.getAllClient();
    }


}
