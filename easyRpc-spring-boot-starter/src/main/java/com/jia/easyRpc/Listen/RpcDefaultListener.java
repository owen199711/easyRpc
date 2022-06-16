package com.jia.easyRpc.Listen;

import com.jia.easyRpc.Client.ClientProxyFactory;
import com.jia.easyRpc.Entity.ServerInfo;
import com.jia.easyRpc.Service.Register.ServerRegister;
import com.jia.easyRpc.annotation.ServiceExpose;
import com.jia.easyRpc.Entity.RpcServerProperty;
import com.jia.easyRpc.Service.RpcServer;
import com.jia.easyRpc.annotation.ServiceReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

public class RpcDefaultListener implements ApplicationListener<ContextRefreshedEvent> {
    private RpcServerProperty rpcServerProperty;//服务配置类
    private ServerRegister serverRegister;//远程服务注册到zk
    private ClientProxyFactory clientProxyFactory;//将服务加载到本地
    private RpcServer rpcServer;//服务类

    public RpcDefaultListener(RpcServerProperty rpcServerProperty, ServerRegister serverRegister,
                              ClientProxyFactory clientProxyFactory, RpcServer rpcServer) {
        this.rpcServerProperty = rpcServerProperty;
        this.serverRegister = serverRegister;
        this.clientProxyFactory = clientProxyFactory;
        this.rpcServer = rpcServer;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("a");
        final ApplicationContext applicationContext=event.getApplicationContext();
        if(applicationContext.getParent()==null){
            //启动服务端
            RpcSeverStart(applicationContext);
            //注册所有暴露出来的服务
            injectDependencyService(applicationContext);
        }
    }

    /**
     * 所有被IOC加载的类中加载包含ServiceReference
     * 需要远程调用的类注入代理对象
     * @param applicationContext
     */
    private void injectDependencyService(ApplicationContext applicationContext) {
        // 遍历容器中所有的 bean
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> clazz = applicationContext.getType(beanName);
            if (clazz == null) {
                continue;
            }

            // 遍历每个 bean 的成员属性，如果成员属性被 @ServiceReference 注解标记，说明依赖rpc远端接口
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                final ServiceReference annotation = field.getAnnotation(ServiceReference.class);
                if (annotation == null) {
                    // 如果该成员属性没有标记该注解，继续找一下
                    continue;
                }
                // 终于找到被注解标记的成员属性了
                Object beanObject = applicationContext.getBean(beanName);
                Class<?> fieldClass = field.getType();
                field.setAccessible(true);
                System.out.println("injectDependencyService-->"+fieldClass.toGenericString()+" "+new Date());
                try {
                    // 注入代理对象值
                    field.set(beanObject, clientProxyFactory.getProxyInstance(fieldClass));
                } catch (IllegalAccessException e) {

                }
            }
        }
    }

    /**
     *遍历所有被IOC加载的服务，被标注ServerExpose
     * 将所有服务注册到zk中
     * @param applicationContext
     */
    private void RpcSeverStart(ApplicationContext applicationContext) {

        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(ServiceExpose.class);
        System.out.println(beans.size()+"---->");
        if (beans.size() == 0) {
            return;
        }

        for (Object obj : beans.values()) {
            final Class<?> clazz = obj.getClass();
            final Class<?>[] interfaces = clazz.getInterfaces();
            // 这里假设只实现了一个接口
            final Class<?> interfaceClazz = interfaces[0];

            final String name = interfaceClazz.getName();
            String ip = "127.0.0.1";
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {

            }
            final Integer port = rpcServerProperty.getPort();
            final ServerInfo serviceInfo = new ServerInfo(name, ip, port, interfaceClazz, obj);

            try {
                // 注册服务
                serverRegister.register(serviceInfo);
            } catch (Exception e) {
            }
        }
        // 启动 rpc 服务器，开始监听端口
          rpcServer.start();
    }


}
