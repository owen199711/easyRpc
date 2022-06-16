package com.jia.easyRpc.Client;



import com.jia.easyRpc.Entity.ServerInfo;
import com.jia.easyRpc.Exception.RpcException;
import com.jia.easyRpc.Protocol.RpcRequest;
import com.jia.easyRpc.Protocol.RpcResponse;
import com.jia.easyRpc.Protocol.MessageProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Random;

public class ClientProxyFactory {

    private RpcClient rpcClient;

    private MessageProtocol messageProtocol;

    private RemoteServers remoteServers;

    public ClientProxyFactory(RpcClient rpcClient, MessageProtocol messageProtocol,
                              RemoteServers remoteServers) {
        this.rpcClient = rpcClient;
        this.messageProtocol = messageProtocol;
        this.remoteServers = remoteServers;
    }

    //需要将服务加载到本地
    @SuppressWarnings("unchecked")
    public <T>T getProxyInstance(Class<?> fieldClazz) {
        return (T) Proxy.newProxyInstance(fieldClazz.getClassLoader(), new Class[]{fieldClazz}, new InvocationHandler() {
            final Random random=new Random();
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                //拿到远程服务段暴露的服务
                List<ServerInfo> lists=remoteServers.getRemoteServerList(fieldClazz.getName());

                if(lists.isEmpty()) throw new RpcException("no server");

                //随机挑选一个服务器上的服务
                ServerInfo serverInfo=lists.get(random.nextInt(lists.size()));
                System.out.println("ClientProxyFactory-->"+serverInfo.toString());

                //构造rpc请求
                final RpcRequest rpcRequest=new RpcRequest();
                rpcRequest.setServerName(fieldClazz.getName());
                rpcRequest.setMethond(method.getName());
                rpcRequest.setParams(objects);
                rpcRequest.setParamTypes(method.getParameterTypes());

                System.out.println("ClientProxyFactory-->"+rpcRequest.toString());
                //编码
                byte[] req=messageProtocol.EncodeReqMsg(rpcRequest);

                //发送信息
                byte[] bytes = rpcClient.sendMsg(req,serverInfo);

                //信息解码
                final RpcResponse rpcResponse=messageProtocol.DecodeRespMsg(bytes);

                if(rpcResponse.getException()!=null) throw rpcResponse.getException();
                return rpcResponse.getRetValue();
            }
        });

    }



}
