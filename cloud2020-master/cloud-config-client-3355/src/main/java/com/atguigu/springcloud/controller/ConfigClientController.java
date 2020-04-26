package com.atguigu.springcloud.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author wsk
 * @date 2020/3/15 22:15
 */
@RestController
//添加注解，使客户端可以感知到配置文件的更新
//最后，为了让客户端的配置文件同步更新，还要 运维人员收到发送一个post 请求给客户端
//curl -X POST "http://localhost:3355/actuator/refresh"
//如果引入了服务总线bus，可以实现对客户端的广播而不用这么麻烦
@RefreshScope
public class ConfigClientController {
    @Value("${config.info}")
    private String configInfo;  //要访问的3344上的信息

    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return configInfo;
    }
}