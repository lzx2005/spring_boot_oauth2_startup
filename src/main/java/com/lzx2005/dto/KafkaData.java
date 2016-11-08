package com.lzx2005.dto;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class KafkaData {
    private final static String key = "f1";
    private String value;

    public KafkaData(String value) {
        this.value = value;
    }

    public static String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "KafkaData{" +
                "value='" + value + '\'' +
                '}';
    }
}
