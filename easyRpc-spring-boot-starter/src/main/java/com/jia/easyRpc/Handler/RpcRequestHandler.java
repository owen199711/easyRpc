package com.jia.easyRpc.Handler;



import com.jia.easyRpc.Entity.ServerInfo;
import com.jia.easyRpc.Protocol.RpcRequest;
import com.jia.easyRpc.Protocol.RpcResponse;
import com.jia.easyRpc.Service.Register.ServerRegister;
import com.jia.easyRpc.Protocol.MessageProtocol;

import java.lang.reflect.Method;

/**
 * Rpc请求的处理器
 */
public class RpcRequestHandler {
    //需要解码和编码
    private MessageProtocol messageProtocol;

    //需要拿到远程服务提供的方法
    private ServerRegister serverRegister;

    public RpcRequestHandler(MessageProtocol messageProtocol, ServerRegister serverRegister) {
        this.messageProtocol = messageProtocol;
        this.serverRegister = serverRegister;
    }

    //拿到请求的信息->解码->反射调用远程服务
    public byte[] handlerReq(byte[] data) throws Exception {
        RpcRequest rpcRequest = messageProtocol.DecodeReqMsg(data);

        String serverName=rpcRequest.getServerName();

        ServerInfo serverInfo=serverRegister.getServerInstance(serverName);

        //响应
        RpcResponse rpcResponse=new RpcResponse();

        if(serverInfo==null){
            rpcResponse.setStatus("not found");
            return messageProtocol.EncodeRespMsg(rpcResponse);
        }

        //调用远程服务的方法
        try{
            final Method method=serverInfo.getClazz().getMethod(rpcRequest.getMethond(),rpcRequest.getParamTypes());
            Object resp = method.invoke(serverInfo.getObj(), rpcRequest.getParams());

            rpcResponse.setStatus("Success");
            rpcResponse.setRetValue(resp);
        }catch (Exception e) {
            rpcResponse.setStatus("Fail");
            rpcResponse.setRetValue(e);
        }
        return messageProtocol.EncodeRespMsg(rpcResponse);

    }
}
