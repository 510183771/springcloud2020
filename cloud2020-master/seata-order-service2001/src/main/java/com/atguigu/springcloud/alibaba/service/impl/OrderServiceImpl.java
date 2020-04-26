package com.atguigu.springcloud.alibaba.service.impl;
import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author nick
 * @date 2020/3/25 20:59
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private AccountService accountService;
    @Resource
    private StorageService storageService;

    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     * 简单说:
     * 下订单->减库存->减余额->改状态
     * GlobalTransactional ç开启分布式事务,异常时回滚,name保证唯一即可
     * 标注了@GlobalTransactional 说明这个是TM，
     * RM 即是这个方法中涉及的三个数据库
     * TC 即是 我们安装的seata服务
     * seata 默认使用的是AT
     *
     * 过程：
     * 当执行订单业务后，首先，会在seata的branch_table 创建三条记录，订单，库存，账户，表中会有分支事务的id和全局事务的id
     * 并且三个表（订单表，库存表，账户表）各自的undo_log 表中也有一条对应的记录
     * undo_log 表中有一个字段，以json的形式，保存了 更新前的记录和更新后的记录，为了回滚的时候可以进行补偿
     * seata 表的global_table 也会有一条记录，seata 表中的lock_table 会有三条lock的记录
     *
     *  提交或回滚后，最后会把seata表和各自undo_log表中与本次事务有关的记录清除
     *
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        log.info("----->开始创建新订单");
        //1 新建订单
        orderDao.createOrder(order);

        log.info("----->订单微服务开始调用库存，做扣减Count");
        //2 扣减库存
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("----->订单微服务开始调用库存，做扣减end");

        log.info("----->订单微服务开始调用账户，做扣减Money");
        //3 扣减账户
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("----->订单微服务开始调用库存，做扣减end");

        //修改订单状态，从0到1，1代表以及完成
        log.info("----->修改订单状态开始");
        orderDao.update(order.getUserId(),0);//update 语句中默认更新为1，status 为0的才会更新
        log.info("----->修改订单状态结束");

        log.info("----->下订单结束了，O(∩_∩)O哈哈~");
    }
}
