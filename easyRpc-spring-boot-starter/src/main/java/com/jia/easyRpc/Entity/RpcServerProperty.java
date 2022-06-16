package com.jia.easyRpc.Entity;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "rpc.server")
@Data
public class RpcServerProperty {

    private String ip;

    private int port;

    private String protocol;

    private String zkAddress;
}
