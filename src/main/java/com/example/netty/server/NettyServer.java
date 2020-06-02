/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.server;

import com.example.netty.common.serialize.MarshallingCodeFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Kongjianfu
 * @date 2020-02-16
 */
public class NettyServer {

    public NettyServer() {

        /**
         * Netty 编程范式
         */
        // 1、创建两个工作线程组：一个用于接收网络请求的；另一个用于实际处理业务的线程组。

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        // 辅助类，帮助我们构建Netty模型
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // buffer 设置多大，具体看业务
                    // 表示缓存区动态调配（自适应），可能存在性能问题。数据包相差不大的时候自适应比较有意义
//                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    // 缓存区 池化操作
//                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    // 日志配置
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 异步的去处理业务
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {

                            // 编码解码，前后顺序无所谓
//                            sc.pipeline().addLast(MarshallingCodeFactory.getEncoder());
//                            sc.pipeline().addLast(MarshallingCodeFactory.getDecoder());
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    });

            // 绑定端对，同步等待请求连接
            ChannelFuture channelFuture = serverBootstrap.bind(8765).sync();
            System.out.println("Server Startup...");
            // 异步的去关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅停机
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("Server Shutdown...");
        }

    }

    public static void main(String[] args) {
        new NettyServer();
    }

}




























