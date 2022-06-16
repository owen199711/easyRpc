package com.jia.easyRpc.Protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求协议
 */
@Data
public class RpcRequest implements Serializable {
    private String ServerName; //服务名
    private String Methond; //服务方法
    private Map<String, String> headers=new HashMap<>(); //请求头
    private Class<?>[] paramTypes;//参数类型
    private Object[] params; //参数
}
