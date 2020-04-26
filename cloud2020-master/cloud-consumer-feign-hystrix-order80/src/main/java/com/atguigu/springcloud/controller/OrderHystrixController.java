package com.atguigu.springcloud.controller;
import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
/**
 * @author wsk
 * @date 2020/3/14 10:24
 */
@RestController
@Slf4j

//为所有方法配置一个默认的fallback 方法，如果方法已经自己配置了一个，则用他自己的
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;

    // 由于fallback 服务降级的已经单独抽离出来，这里不用添加任何配置了
    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    //设置自己的等待超时时间，1.5秒后服务端没有返回，就直接到fallback里面处理
    /*@HystrixCommand(fallbackMethod = "paymentTimeOutFallBackMethod",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")
    })*/
    @HystrixCommand //没有配置自己的fallback，用默认的
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        int age = 10/0;
        return paymentHystrixService.paymentInfo_TimeOut(id);
    }

    public String paymentTimeOutFallBackMethod(@PathVariable("id") Integer id){
        return "我是消费者80，对方支付系统繁忙，请稍后再试，o(╥﹏╥)o";
    }
    /**
     * 全局 fallback 方法
     * @return
     */
    public String payment_Global_FallbackMethod(){
        return "Global异常处理信息，请稍后再试。/(╥﹏╥)/~~";
    }
}