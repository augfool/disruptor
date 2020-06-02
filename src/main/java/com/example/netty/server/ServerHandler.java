/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.server;

import com.example.netty.common.entity.TranslatorData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Kongjianfu
 * @date 2020-02-17
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        TranslatorData request = (TranslatorData) msg;
        System.out.println("Server端: id= " + request.getId() +
                           ", name= " + request.getName() + ", Message= " + request.getMessage());

        TranslatorData response = new TranslatorData();
        response.setId("response: " + request.getId());
        response.setName("response: " + request.getName());
        response.setMessage("response: " + request.getMessage());

        // 写出response响应信息
        // 写操作中已经把读操作的buffer释放了
        ctx.writeAndFlush(response);

    }
}
