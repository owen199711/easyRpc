package com.jia.easyRpc.Protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultMessageProtocol implements MessageProtocol {
    @Override
    public byte[] EncodeReqMsg(RpcRequest rpcRequest) throws Exception {
        return serialize(rpcRequest);
    }

    @Override
    public byte[] EncodeRespMsg(RpcResponse rpcResponse) throws Exception {
        return serialize(rpcResponse);
    }

    @Override
    public RpcRequest DecodeReqMsg(byte[] data) throws Exception {
        ObjectInputStream os=new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcRequest)os.readObject();
    }

    @Override
    public RpcResponse DecodeRespMsg(byte[] data) throws Exception {
        ObjectInputStream os=new ObjectInputStream(new ByteArrayInputStream(data));
        return (RpcResponse)os.readObject();
    }

    private byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        return bos.toByteArray();
    }
}
