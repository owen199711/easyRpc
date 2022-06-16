package com.jia.easyRpc.Service.Register;


import com.jia.easyRpc.Entity.ServerInfo;

/**
 * 提供服务注册的接口
 */
public interface ServerRegister {
    //注册服务
    void register(ServerInfo serverInfo) throws Exception;

    //获取服务对象
    ServerInfo getServerInstance(String serverName) throws Exception;
}
