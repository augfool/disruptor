/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

import com.lmax.disruptor.EventHandler;

/**
 *
 * 具体的消费者（数据监听）
 * 事件驱动模式
 *
 * @author study
 * @date 2019-09-07
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {

        System.out.println("消费者：" + event.getValue());

    }
}
