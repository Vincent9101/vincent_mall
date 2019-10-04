package com.vincent.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: Vincent
 * @created: 2019/10/4  14:13
 * @description:缓存 token
 */
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static LoadingCache<String, String> tokenCache = CacheBuilder.newBuilder()
            .initialCapacity(1000)
            .maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认获取值方式
                @Override
                public String load(String s) throws Exception {
                    return "";
                }
            });

    public static void put(String key, String value) {
        tokenCache.put(key, value);
    }

    public static String get(String key) {
        String val;
        try {
            val = tokenCache.get(key);
            return val;
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
