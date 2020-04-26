package com.atguigu.springcloud.service;
import org.springframework.stereotype.Component;

/**
 * 由于把fallback的逻辑添加controller层不太好，跟业务代码混合在一起了
 * 这里单独把fallback的逻辑抽离出来，但是需要为每一个方法实现fallback
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return "----PaymentFallbackService fall back-paymentInfo_OK,o(╥﹏╥)o";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "----PaymentFallbackService fall back-paymentInfo_TimeOut,o(╥﹏╥)o";
    }
}