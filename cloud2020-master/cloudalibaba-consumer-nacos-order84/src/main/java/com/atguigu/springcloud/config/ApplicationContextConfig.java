package com.atguigu.springcloud.config;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * @author wsk
 * @date 2020/3/24 23:03
 */
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced//ribbon 负载均衡
    public RestTemplate getTemplate(){
        return new RestTemplate();
    }
}