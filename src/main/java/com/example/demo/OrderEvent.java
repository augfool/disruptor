/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo;

/**
 * Event: 从生产者到消费者过程中所处理的数据单元。
 * Disruptor中没有代码表示Event，因为它完全是由用户自定义的。
 *
 * @author study
 * @date 2019-09-07
 */
public class OrderEvent {

    /**
     * 订单价格
     */
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
