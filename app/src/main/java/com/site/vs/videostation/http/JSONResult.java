package com.site.vs.videostation.http;

/**
 */
public class JSONResult<T> {
    public int code;
    public String message;
    public String error;
    public T data;
}
