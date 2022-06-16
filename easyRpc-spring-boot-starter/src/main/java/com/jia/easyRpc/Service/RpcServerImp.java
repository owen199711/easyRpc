package com.jia.easyRpc.Service;

import com.jia.easyRpc.Handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class RpcServerImp extends RpcServer {

    private Channel channel;
    public RpcServerImp(int port, String protocol, RpcRequestHandler rpcRequestHandler) {
        super(port, protocol, rpcRequestHandler);
    }

    @Override
    public void start() {
        //使用Netty进行网络通信

        //创建两个线程组
        EventLoopGroup boosGroup=new NioEventLoopGroup(1);
        EventLoopGroup wokerGroup=new NioEventLoopGroup(8);

        //创建服务配置类
        ServerBootstrap bootstrap=new ServerBootstrap();

        //通过链式编程配置
        try{
            bootstrap.group(boosGroup,wokerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RpcChannelHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.bind(port).sync();
            channel =cf.channel();

            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }

    }
    @Override
    public void stop() {
        channel.close();
    }
    private class RpcChannelHandler extends ChannelInboundHandlerAdapter {

        //接受从客户端发来的消息，
        //通过服务段处理后，返回处理后的消息
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf = (ByteBuf) msg;//客户端发来的请求消息
            byte[] reqBuf=new byte[buf.readableBytes()];
            buf.readBytes(reqBuf);

            //拿到响应的信息
            byte[] respBuf = rpcRequestHandler.handlerReq(reqBuf);
            ByteBuf rb= Unpooled.buffer(reqBuf.length);
            rb.writeBytes(respBuf);
            ctx.writeAndFlush(rb);

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.channel().close();
        }
    }

}
