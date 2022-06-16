package com.jia.easyRpc.Protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应协议
 */
@Data
public class RpcResponse implements Serializable {
    private String status;
    private Map<String, String> headers=new HashMap<>();
    private Object retValue;
    private Exception exception;
}
