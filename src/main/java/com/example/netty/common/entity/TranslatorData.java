/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.common.entity;

import java.io.Serializable;

/**
 * @author Kongjianfu
 * @date 2020-02-16
 */
public class TranslatorData implements Serializable {

    private static final long serialVersionUID = -8898083917690721432L;

    private String id;

    private String name;

    /**
     * 传输消息体内容
     */
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TranslatorData{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", message='" + message + '\'' + '}';
    }
}
