/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.demo.height.chain;

import java.util.UUID;

import com.lmax.disruptor.EventHandler;

/**
 * @author Kongjianfu
 * @date 2020-02-15
 */
public class Handler2 implements EventHandler<Trade> {

    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {

        System.out.println("handler 2 : SET ID");
        event.setId(UUID.randomUUID().toString());
        Thread.sleep(1000);

    }
}
