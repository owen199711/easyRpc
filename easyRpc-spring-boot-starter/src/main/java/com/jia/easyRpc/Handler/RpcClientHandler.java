package com.jia.easyRpc.Handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CountDownLatch;


public class RpcClientHandler extends ChannelInboundHandlerAdapter {
    private byte[] data;
    private byte[] response;
    private CountDownLatch countDown=new CountDownLatch(1);

    public RpcClientHandler(byte[] data) {
        this.data = data;
    }

    //连接后就发送信息到服务端
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf= Unpooled.buffer(data.length);
        buf.writeBytes(data);
        ctx.writeAndFlush(buf);
    }

    //将从服务端发来的信息放到response中
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf=(ByteBuf) msg;
        response=new byte[buf.readableBytes()];
        buf.readBytes(response);
        countDown.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }

    public byte[] getResponse() {
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

}
