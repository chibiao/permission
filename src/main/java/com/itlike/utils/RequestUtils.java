package com.itlike.utils;


import javax.servlet.http.HttpServletRequest;

public class RequestUtils {
    /*创建本地线程变量*/
    private static ThreadLocal<HttpServletRequest> local=new ThreadLocal<>();
    public static HttpServletRequest getRequest(){
        return local.get();
    }
    public static void setRequest(HttpServletRequest request){
        local.set(request);
    }
}
