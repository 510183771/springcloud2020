package com.atguigu.springcloud.alibaba.controller;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author wsk
 * @date 2020/3/24 14:00
 */
@RestController
@Slf4j
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA(){
        return "--------testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return "--------testB";
    }

    @GetMapping("/testE")
    public String testE(){
        log.info("testE测试异常数");
        int age = 10/0;
        return "--------testE 测试异常数";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
    //这里的value 值可以任意起，但是一般都是于mapping 保持一致
    //blockHandler 指定处理异常的方法
    //如果加了SentinelResource 注解，但是没有加blockHandler，会报500 的错误，页面不友好
    //添加了SentinelResource注解，异常处理的方法只会处理不符合sentinel规则的异常，其他异常还是会直接500页面
    // 对于这种运行时异常，可以添加fallback，指定处理方法
    //SentinelResource 支持private 方法
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p1",required = false) String p2){
        //int i = 10/0；sentinel 捕获不了
        return "testHotKey";
    }
    public String deal_testHotKey(String p1, String p2, BlockException exception){
        return "---------------deal_testHotKey,o(╥﹏╥)o";
    }

}