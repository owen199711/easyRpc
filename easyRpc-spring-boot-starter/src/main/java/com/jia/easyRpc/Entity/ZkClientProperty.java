package com.jia.easyRpc.Entity;


import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  连接zoomkepper 配置信息
 */
@Component
@ConfigurationProperties(prefix = "rpc.zk")
@Data
public class ZkClientProperty {

    private String connectionString;

    private int sessionTime;

    private int connectionTime;
    private RetryPolicy retryPolicy=new ExponentialBackoffRetry(3000,10);
}
