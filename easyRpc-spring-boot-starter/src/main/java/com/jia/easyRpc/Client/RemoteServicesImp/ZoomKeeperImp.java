package com.jia.easyRpc.Client.RemoteServicesImp;

import com.alibaba.fastjson.JSON;
import com.jia.easyRpc.Exception.EncodeException;
import com.jia.easyRpc.Client.RemoteServers;
import com.jia.easyRpc.Entity.ServerInfo;
import com.jia.easyRpc.Exception.RpcException;
import org.apache.curator.framework.CuratorFramework;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZoomKeeperImp implements RemoteServers {
    /**
     * 远程服务已经注册到 zk中，通过uri拿出这些服务
     */
    private CuratorFramework client;

    public ZoomKeeperImp(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public List<ServerInfo> getRemoteServerList(String serviceName){
        try{
            String serversPath="/rpc/"+serviceName+"/server";
            System.out.println("ZoomKeeperImp-->"+serversPath);
            final List<String> strings = client.getChildren().forPath(serversPath);
            return Optional.ofNullable(strings)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(node -> {
                        try {
                            // 将服务信息经过 URL 解码后反序列化为对象
                            String serviceInstanceJson = URLDecoder.decode(node, "UTF-8");
                            return JSON.parseObject(serviceInstanceJson, ServerInfo.class);
                        } catch (UnsupportedEncodingException e) {
                            throw new EncodeException("encoding error");
                        }
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());

        }catch (Exception e){
            throw  new RpcException("do not have the servers");
        }
    }
}
