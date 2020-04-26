package com.atguigu.springcloud.service;
import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 应该要写一个接口 而不是直接写一个类的，这里只是为了演示方便
 * 使用Jmeter进行压力测试，2w个线程同时访问timeout 方法，结果其他方法也受到
 * 了影响。
 */
@Service
public class PaymentService {
    /**
     * 正常访问
     * @param id
     * @return
     */
    public String paymentInfo_OK(Integer id){
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_OK,id："+id+"\t"+"O(∩_∩)O哈哈~";
    }

    /**
     * @HystrixCommand报异常后如何处理：
     * 一旦调用服务方法失败并抛出了错误信息后，
     * 会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法
     *
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            //设置这个线程的超时时间是3s，3s内是正常的业务逻辑，超过3s调用fallbackMethod指定的方法进行处理
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
    })
    public String paymentInfo_Timeout(Integer id){
        int timeNumber = 5;

        //经测试，除了超时异常，其他异常也可以被 paymentInfo_TimeOutHandler 处理
        int age = 10/0;
        /*try{
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }*/
        return "线程池："+Thread.currentThread().getName()+"   paymentInfo_Timeout,id："+id+"\t"+"O(∩_∩)O哈哈~"+"   耗时(秒)："+timeNumber;
    }

    //如果paymentInfo_Timeout 异常了，提供一个fallback的处理方法
    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池："+Thread.currentThread().getName()+"   系统繁忙，请稍后再试,id："+id+"\t"+"o(╥﹏╥)o";
    }

    //为服务添加一个熔断
    // 例如 如果在10秒内，请求10次，失败率达到60% 就启动熔断。当 服务熔断生效后，即使再有请求进来，也会使用
    // fallback来处理，当过一个时间后（默认是5秒），他会自动恢复调用链路，可以正常处理请求了
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),   //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),  //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),    //时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),    //失败率达到多少后跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id < 0){
            throw new RuntimeException("******id 不能为负数");
        }

        //IdUtil hutool 工具包里面的工具，这个工具包是中国人开发，里面有很多常用的工具包可以直接使用
        String serialNumber = IdUtil.simpleUUID();  //UUID.randomUUID();

        return Thread.currentThread().getName()+"\t"+"调用成功，流水号："+serialNumber;
    }

    //服务熔断的fallback处理方法
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能负数，请稍后再试，o(╥﹏╥)o  id："+id;
    }



}