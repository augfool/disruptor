/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.chain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 交易信息类
 * Disruptor 中的 Event
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Trade {

    private String id;

    private String name;

    private double price;

    /**
     * CAS 操作，线程安全
     */
    private AtomicInteger count = new AtomicInteger();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}
