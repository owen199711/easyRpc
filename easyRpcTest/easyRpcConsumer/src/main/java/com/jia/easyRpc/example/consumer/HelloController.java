package com.jia.easyRpc.example.consumer;

import com.jia.easyRpc.annotation.ServiceReference;
import com.jia.easyRpc.example.provider.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    // 通过自定义注解注入远端服务
    @ServiceReference
    private HelloService helloService;
    @GetMapping("/hello/{name}")
    public String hello(@PathVariable String name) {
        //调用远程服务
        final String rsp = helloService.sayHello(name);
        logger.info("Receive message from rpc server, msg: {}", rsp);
        return rsp;
    }
}
