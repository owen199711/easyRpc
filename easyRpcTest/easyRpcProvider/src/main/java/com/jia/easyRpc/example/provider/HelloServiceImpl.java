package com.jia.easyRpc.example.provider;


import com.jia.easyRpc.annotation.ServiceExpose;
import com.jia.easyRpc.example.provider.api.HelloService;

@ServiceExpose
public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return "「来Owen的问候」：hello " + name + " , 哇咔咔，你太腻害了" +
                ",这都被你做出来了！";
    }
}
