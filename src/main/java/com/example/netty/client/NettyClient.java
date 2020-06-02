/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.client;

import com.example.netty.common.entity.TranslatorData;
import com.example.netty.common.serialize.MarshallingCodeFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Kongjianfu
 * @date 2020-02-18
 */
public class NettyClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;

    /**
     * 扩展 完善ConcurrentHashMap<String, Channel>
     */
    private Channel channel;

    // 1、创建一个工作线程组 :用于实际处理业务的线程组。
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelFuture channelFuture;

    public NettyClient() {
        this.connect(HOST, PORT);
    }

    private void connect(String host, int port) {

        // 2、 辅助类（注意Client 和 Server 不一样），帮助我们构建Netty模型
        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    // buffer 设置多大，具体看业务
                    // 表示缓存区动态调配（自适应），可能存在性能问题。数据包相差不大的时候自适应比较有意义
//                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    // 缓存区 池化操作
//                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    // 日志配置
//                    .handler(new LoggingHandler(LogLevel.INFO))

                    // 异步的去处理业务
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {

                            // 编码解码，前后顺序无所谓
//                            sc.pipeline().addLast(MarshallingCodeFactory.getEncoder());
//                            sc.pipeline().addLast(MarshallingCodeFactory.getDecoder());
                            sc.pipeline().addLast(new ClientHandler());
                        }
                    });

            // 绑定端对，同步阻塞
            this.channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("Client connected...");

            // 接下来就进行数据的发送：但是首先我们要获取channel
            this.channel = channelFuture.channel();

            // 异步的去关闭
            // channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() throws Exception{
        // 异步的去关闭
        channelFuture.channel().closeFuture().sync();
        // 优雅停机
        workGroup.shutdownGracefully();
        System.out.println("Client Shutdown...");
    }

    public void sendData() {
        for (int i = 0; i < 10; i++) {
            TranslatorData request = new TranslatorData();
            request.setId("" + i);
            request.setName("请求消息名称" + i);
            request.setMessage("请求消息内容" + i);
            this.channel.writeAndFlush(request);
        }

    }

    public static void main(String[] args) {
        new NettyClient().sendData();
    }

}
