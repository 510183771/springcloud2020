package com.atguigu.springcloud;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 如果一起动就报错，很有可能是版本不一致，也就是依赖包的版本与你安装的zookeeper的版本不一致
 * 注册到zookeeper 的微服务都是临时节点，如果微服务挂了， 那么zookeeper 会立刻删除该微服务
 */
@SpringBootApplication
@EnableDiscoveryClient  //该注解用于想使用consul或者zookeeper作为注册中心时注册服务
public class PaymentMain8004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8004.class,args);
    }
}