package com.atguigu.springcloud.service;
import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

/**
 * 创建一个专门存放错误处理都类，这样都好处是不用把这些代码放在业务层
 * @author nick
 * @date 2020/3/25 9:06
 */
@Component
public class PaymentFallbackService implements PaymentService{
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(444,"服务降级返回，------------PaymentFallbackService",new Payment(id,"errorSerial"));
    }
}
