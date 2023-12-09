package com.example.android_demo.utils;

import java.util.Map;

public class ResponseData {
    private String  code;
    private String message;
    private Map<String, String> data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public Map<String, String> getData() {
        return data;
    }
}
