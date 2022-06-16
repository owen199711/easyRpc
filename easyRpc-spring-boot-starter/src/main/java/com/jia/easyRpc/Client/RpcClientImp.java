package com.jia.easyRpc.Client;

import com.jia.easyRpc.Entity.ServerInfo;
import com.jia.easyRpc.Handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class RpcClientImp implements RpcClient {
    @Override
    public byte[] sendMsg(byte[] data, ServerInfo serverInfo) throws Exception {
        String ip=serverInfo.getIp();
        int port=serverInfo.getPort();
        RpcClientHandler rpcClientHandler=new RpcClientHandler(data);

        //创建线程组
        EventLoopGroup workGroup=new NioEventLoopGroup(8);
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(rpcClientHandler);
                    }
                });

        bootstrap.connect(ip, port).sync();

        return rpcClientHandler.getResponse();
    }
}
