package com.jia.easyRpc.Protocol;

public interface MessageProtocol {
    //编码请求参数
    byte[] EncodeReqMsg(RpcRequest rpcRequest) throws Exception;
    //编码响应参数
    byte[] EncodeRespMsg(RpcResponse rpcResponse) throws Exception;
    //解码请求参数
    RpcRequest DecodeReqMsg(byte[] data) throws Exception;
    //解码响应参数
    RpcResponse DecodeRespMsg(byte[] data) throws Exception;
}
