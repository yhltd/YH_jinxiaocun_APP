package com.example.myapplication;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {
    private static CacheManager instance;
    private Map<String, Object> cache = new HashMap<>();

    // 私有构造函数
    private CacheManager() {
        // 初始化时可以做一些设置
    }

    // 获取单例实例
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    // 存储数据
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    // 获取数据
    public Object get(String key) {
        return cache.get(key);
    }

    // 获取数据（带默认值）
    public Object get(String key, Object defaultValue) {
        Object value = cache.get(key);
        return value != null ? value : defaultValue;
    }

    // 专门处理 shujuku 值的方法
    public int getShujukuValue() {
        Object value = cache.get("shujuku");
        return value != null ? (int) value : 0;
    }

    public void setShujukuValue(int value) {
        cache.put("shujuku", value);
    }

    // 专门处理查询结果的方法
    public Object getQueryResult() {
        return cache.get("query_result");
    }

    public void setQueryResult(Object result) {
        cache.put("query_result", result);
    }

    // 移除数据
    public void remove(String key) {
        cache.remove(key);
    }

    // 清空所有缓存
    public void clear() {
        cache.clear();
    }

    // 检查是否包含某个key
    public boolean contains(String key) {
        return cache.containsKey(key);
    }
}