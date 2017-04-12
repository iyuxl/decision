package com.shj.entity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xiaoyaolan on 2017/4/7.
 */
public class FactResult extends ConcurrentHashMap {
    public FactResult(String ruleName) {
        this.put("ruleName", ruleName);
    }
}
