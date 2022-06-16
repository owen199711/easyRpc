package com.jia.easyRpc.Service;


import com.jia.easyRpc.Handler.RpcRequestHandler;

public abstract class RpcServer {
    protected int port;
    protected String protocol;
    protected RpcRequestHandler rpcRequestHandler;

    public RpcServer(int port, String protocol, RpcRequestHandler rpcRequestHandler) {
        this.port = port;
        this.protocol = protocol;
        this.rpcRequestHandler = rpcRequestHandler;
    }

    public abstract void start();
    public abstract void stop();
}
