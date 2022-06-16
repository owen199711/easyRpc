package com.jia.easyRpc.Config;
import com.jia.easyRpc.Client.ClientProxyFactory;
import com.jia.easyRpc.Client.RemoteServicesImp.ZoomKeeperImp;
import com.jia.easyRpc.Client.RpcClientImp;
import com.jia.easyRpc.Entity.ZkClientProperty;
import com.jia.easyRpc.Listen.RpcDefaultListener;
import com.jia.easyRpc.Protocol.DefaultMessageProtocol;
import com.jia.easyRpc.Service.Register.ServerRegister;
import com.jia.easyRpc.Service.RpcServerImp;
import com.jia.easyRpc.Client.RemoteServers;
import com.jia.easyRpc.Client.RpcClient;
import com.jia.easyRpc.Entity.RpcServerProperty;
import com.jia.easyRpc.Handler.RpcRequestHandler;
import com.jia.easyRpc.Protocol.MessageProtocol;
import com.jia.easyRpc.Service.Register.zkServerRegister;
import com.jia.easyRpc.Service.RpcServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
/**
 * 初始加载类
 */
public class AutoConfiger {

    @Bean
    RpcServerProperty rpcServerProperty(){
        return new RpcServerProperty();
    }

    @Bean
    ZkClientProperty zkClientProperty(){
        return new ZkClientProperty();
    }

    @Bean
    RpcDefaultListener rpcDefaultListener(@Autowired RpcServerProperty rpcServerProperty, @Autowired ServerRegister serverRegister,
                                          @Autowired ClientProxyFactory clientProxyFactory, @Autowired RpcServer rpcServer){
        System.out.println(1);
        return new RpcDefaultListener(rpcServerProperty,serverRegister,clientProxyFactory,rpcServer);
    }

    @Bean
    CuratorFramework zkClient(@Autowired ZkClientProperty zk){
        System.out.println(2);
        System.out.println(zk.getConnectionString()+","+zk.getConnectionTime());
        CuratorFramework client=CuratorFrameworkFactory.newClient(zk.getConnectionString(),
                zk.getSessionTime(),zk.getConnectionTime(),zk.getRetryPolicy());
        client.start();
        return client;
    }

    @Bean
    RemoteServers remoteServers(@Autowired CuratorFramework zkClient){
        System.out.println(3);
        return new ZoomKeeperImp(zkClient);
    }

    @Bean
    RpcClient rpcClient(){
        System.out.println(4);

        return new RpcClientImp();
    }

    @Bean
    ServerRegister serverRegister(@Autowired CuratorFramework zkClient){
        System.out.println(5);
        return new zkServerRegister(zkClient);
    }

    @Bean
    MessageProtocol messageProtocol(){
        System.out.println(6);
        return new DefaultMessageProtocol();
    }

    @Bean
    RpcRequestHandler rpcRequestHandler(@Autowired MessageProtocol messageProtocol, @Autowired ServerRegister serverRegister){
        System.out.println(7);
        return new RpcRequestHandler(messageProtocol,serverRegister);
    }

    @Bean
    RpcServer rpcServer(@Autowired RpcRequestHandler rpcRequestHandler, @Autowired RpcServerProperty rpcServerProperty){
        System.out.println(8);
        return new RpcServerImp(rpcServerProperty.getPort(),rpcServerProperty.getProtocol(),rpcRequestHandler);
    }

    @Bean
    ClientProxyFactory clientProxyFactory(@Autowired RpcClient rpcClient, @Autowired MessageProtocol messageProtocol,
                                          @Autowired RemoteServers remoteServers){
        System.out.println(9);
        return new ClientProxyFactory(rpcClient,messageProtocol,remoteServers);
    }



}
