/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.lmax.disruptor.mytest;

import com.lmax.disruptor.EventFactory;

/**
 * @author study
 * @date 2019-09-07
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {

    @Override
    public OrderEvent newInstance() {

        // 这个方法就是为了返回空的数据对象（Event）
        return new OrderEvent();
    }
}
