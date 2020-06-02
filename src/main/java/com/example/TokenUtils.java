/*
 * Baijiahulian.com Inc. Copyright (c) 2014-2019 All Rights Reserved.
 */

package com.example;

import org.apache.commons.codec.digest.DigestUtils;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kongjianfu
 * @date 2020-02-22
 */
public class TokenUtils {

    /**
     * 生成接口签名 *
     * @param uri 请求接口路径
     * @param method HTTP 方法
     * @param secret 分配密钥
     * @param params URL 参数列表 * @return
     */
    public static String generateToken(String uri, String method, String secret, Map<String, String[]> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(secret).append("&");
        builder.append(method.toUpperCase()).append("&");
        builder.append(encode(uri));
        Map<String, String[]> cloneMap = new HashMap<>(params);
        cloneMap.remove("token");
        String[] keys = sortKeys(cloneMap.keySet().toArray(new String[0]));
        StringBuilder kv = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String[] values = params.get(key);
            if (values != null && values.length > 0) {
                kv.append(key).append("=").append(values[0]);
            }
            if (i < (keys.length - 1)) {
                kv.append("&");
            }
        }
        System.out.println(kv.toString());
        if (kv.length() > 0) {
            builder.append("&").append(encode(kv.toString()));
        }
        System.out.println(builder.toString());
        return DigestUtils.md5Hex(builder.toString());
    }

    /**
     * key 排序
     * @param keys key 数组
     * @return 排序后的 key 数组 */
    public static String[] sortKeys(String[] keys) { for (int i = 0; i < keys.length - 1; i++) {
        for (int j = i + 1; j < keys.length; j++) { String temp = "";
            if (keys[i].compareTo(keys[j]) > 0) { temp = keys[j];
                keys[j] = keys[i];
                keys[i] = temp; }
        } }
        return keys;
    }

    /**
     * UTF-8 编码
     * @param value 待编码数据 * @return 编码后数据
     */
    private static String encode(String value) { String result = null;
        try {
            result = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) { result = "";
        }
        return result;
    }

    public static void main(String[] args) {

//        String protocol = "https://";
//        String host = "专稿系统域名";
//        String uri = "/path/to/request/api";
//        String method = "GET";
//        String secret = "your_secret_key";
//        Long timestamp = System.currentTimeMillis();
//        Map<String, String[]> params = new HashMap<>();
//        params.put("access_key", new String[]{"your_access_key"});
//        params.put("timestamp", new String[]{timestamp.toString()});
//        params.put("other_param_key", new String[]{"other_param_value"});


        String a = "http://www.baidu.com";
        String method = "b";
        String secret = "c";
        Map<String, String[]> params = new HashMap<>();
        params.put("c", new String[]{"abs", "sdew"});
        params.put("a", new String[]{"sd"});
        params.put("b", new String[]{"1"});

        String token = TokenUtils.generateToken(a, method, secret, params);
        System.out.println(token);

    }

}
