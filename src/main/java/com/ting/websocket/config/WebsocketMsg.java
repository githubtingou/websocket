package com.ting.websocket.config;

import lombok.Data;

import java.util.List;

/**
 * websocket入场
 *
 * @author ting
 * @version 1.0
 * @date 2021/9/11
 */
@Data
public class WebsocketMsg {

    /**
     * 发送者
     */
    private String sender;

    /**
     * 发送的数据
     */
    private String msg;

    /**
     * 是否群发
     */
    private boolean isSendAll;

    /**
     * 是否给自己发送消息
     */
    private boolean isSendMe;

    /**
     * 用来标识信息格式,主要用于前端渲染
     */
    private Integer flag;

    /**
     * 用户集合
     */
    private List<String> userIdList;
}
