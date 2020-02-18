/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example.netty.common.serialize;

import com.example.netty.common.entity.TranslatorData;
import com.google.gson.Gson;

/**
 * @author Kongjianfu
 * @date 2020-02-18
 */
public class GsonUtil {

    private static Gson gson = new Gson();

    public static String encode(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T decode(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static void main(String[] args) {
        TranslatorData data = new TranslatorData();
        data.setId("1");
        data.setName("ok");
        data.setMessage("hello world");
        String json = GsonUtil.encode(data);
        System.out.println(json);
        TranslatorData data1 = GsonUtil.decode(json, TranslatorData.class);
        System.out.println(data1);
    }

}
