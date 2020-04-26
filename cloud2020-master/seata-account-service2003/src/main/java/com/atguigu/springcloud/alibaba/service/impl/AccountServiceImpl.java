package com.atguigu.springcloud.alibaba.service.impl;
import com.atguigu.springcloud.alibaba.dao.AccountDao;
import com.atguigu.springcloud.alibaba.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author wsk
 * @date 2020/3/25 22:10
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Resource
    AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info("------>account-service中扣减账户余额开始");
        //模拟超时异常，全局事务回滚
//        try {
//            //暂停20秒，由于Feign的超时默认是1秒，所以肯定会超时
//            //订单服务创建订单后，然后减去库存，然后在减去账户的时候超时，页面报错
//            //但实际上，当超时时间过后，账户也会成功，就会造成订单状态还是0，但是
//            //库存和账户都扣减了
//            TimeUnit.SECONDS.sleep(20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        accountDao.decrease(userId,money);
        LOGGER.info("------>account-service中扣减账户余额结束");
    }
}
