/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {


    // EventHandler
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }

    // WorkHandler
    @Override
    public void onEvent(Trade event) throws Exception {

        System.out.println("handler1 : SET Name");
        event.setName("H1");
        Thread.sleep(1000);

    }
}
