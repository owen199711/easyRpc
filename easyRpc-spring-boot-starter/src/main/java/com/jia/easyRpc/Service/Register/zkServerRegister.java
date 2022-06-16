package com.jia.easyRpc.Service.Register;

import com.alibaba.fastjson.JSON;
import com.jia.easyRpc.Entity.ServerInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;


import java.net.URLEncoder;

public class zkServerRegister extends DefauletServerRegister implements ServerRegister {


    private CuratorFramework zkClient;

    public zkServerRegister(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void register(ServerInfo serverInfo) throws Exception {
        super.register(serverInfo);
        //将服务放入zk中
        String uri= JSON.toJSONString(serverInfo);

        uri= URLEncoder.encode(uri,"UTF-8");

        String rpcPath="/rpc/"+serverInfo.getServiceName()+"/server";

        if(zkClient.checkExists().forPath(rpcPath)==null){
            System.out.println(rpcPath+"不存在");
            zkClient.create().creatingParentContainersIfNeeded().forPath(rpcPath);
        }

        String uriPath=rpcPath+ "/" +uri;

        if(zkClient.checkExists().forPath(uriPath)!=null){
            System.out.println(uriPath+"---存在");
            zkClient.delete().forPath(uriPath);
        }
        System.out.println(uriPath);
        zkClient.create().withMode(CreateMode.EPHEMERAL).forPath(uriPath);

    }
}
