package com.jia.easyRpc.Entity;

import lombok.Data;

/**
 * 远程服务段对外暴露的 服务描述
 */
@Data
public class ServerInfo {
    private String ServiceName; //名称

    private String ip; //地址

    private int port;  //端口

    private Class<?> clazz; //类信息

    private Object obj; //对象

    public ServerInfo() {
    }

    public ServerInfo(String serviceName, String ip, int port) {
        ServiceName = serviceName;
        this.ip = ip;
        this.port = port;
    }

    public ServerInfo(String serviceName, String ip, int port, Class<?> clazz, Object obj) {
        ServiceName = serviceName;
        this.ip = ip;
        this.port = port;
        this.clazz = clazz;
        this.obj = obj;
    }
}
