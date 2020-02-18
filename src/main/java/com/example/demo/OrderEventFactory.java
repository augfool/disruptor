/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

import com.lmax.disruptor.EventFactory;

/**
 *
 * 订单工厂，创建一个个订单对象
 * 消息（event）工厂对象
 *
 * @author study
 * @date 2019-09-07
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {

        // 这个方法就是为了返回空的数据对象（在Disruptor中数据叫做Event）
        return new OrderEvent();
    }
}
