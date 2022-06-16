package com.jia.easyRpc.Client;



import com.jia.easyRpc.Entity.ServerInfo;

import java.util.List;

/**
 * 本机需要通过服务名获取 远程服务端提供的服务列表
 */
public interface RemoteServers {
    List<ServerInfo> getRemoteServerList(String serviceName); //远程提供的服务列表
}
