package com.atguigu.springcloud.alibaba.handler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
/**
 * 定义一个统一的用来处理错误类，这样错误的方法就不用写在controller代码了，
 * 实现了业务代码和错误处理代码的分离
 * @author nick
 * @date 2020/3/24 22:04
 */
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException exception){
        return new CommonResult(4444,"按客户自定义，global handlerException-----1");
    }
    public static CommonResult handlerException2(BlockException exception){
        return new CommonResult(4444,"按客户自定义，global handlerException-----2");
    }
}