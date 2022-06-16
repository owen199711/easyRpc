package com.jia.easyRpc.Client;


import com.jia.easyRpc.Entity.ServerInfo;

/**
 * Rpc客户端，接受本地请求，调用远程服务
 */
public interface RpcClient {
    byte[] sendMsg(byte[] data, ServerInfo serverInfo) throws Exception;
}
