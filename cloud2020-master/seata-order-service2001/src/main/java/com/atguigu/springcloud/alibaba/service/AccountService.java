package com.atguigu.springcloud.alibaba.service;
import com.atguigu.springcloud.alibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

/**
 * @author wsk
 * @date 2020/3/25 21:00
 */
@FeignClient(value = "seata-account-service")//Feign client 默认对超时时间是1秒
public interface AccountService {

    //因为是更新操作，所有用都是post
    @PostMapping(value = "/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId,@RequestParam("count") BigDecimal money);
}
