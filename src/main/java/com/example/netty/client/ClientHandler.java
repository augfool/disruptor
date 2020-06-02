/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.client;

import com.example.netty.common.entity.TranslatorData;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Kongjianfu
 * @date 2020-02-18
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {

            TranslatorData response = (TranslatorData)msg;
            System.out.println("Client端: id= " + response.getId() +
                    ", name= " + response.getName() +
                    ", message= " + response.getMessage());
        } finally {
            // 数据都是用buffer，Netty中一定要注意，用完了要释放
            ReferenceCountUtil.release(msg);
        }

    }
}
