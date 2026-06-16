package com.example.seckill.service;

import com.example.seckill.entity.Order;
import com.example.seckill.repository.GoodsRepository;
import com.example.seckill.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeckillService {

    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;
    private final RedissonClient redissonClient;
    private final RocketMQTemplate rocketMQTemplate;
    private final TransactionTemplate transactionTemplate;

    public String seckill(Long userId, Long goodsId) {
        String lockKey = "seckill:lock:" + userId + ":" + goodsId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 试锁等待时间3秒，锁定持有时间5秒
            boolean isLocked = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                return "当前抢购排队人数较多，请稍候";
            }

            // 使用 TransactionTemplate 在锁的保护范围内进行原子性事务提交
            return transactionTemplate.execute(status -> {
                // 1. 扣减库存
                int updatedRows = goodsRepository.decreaseStock(goodsId);
                if (updatedRows == 0) {
                    // 抛出异常会使本段事务回滚
                    throw new RuntimeException("库存不足，抢购结束！");
                }

                // 2. 生成订单
                Order order = new Order();
                order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
                order.setUserId(userId);
                order.setGoodsId(goodsId);
                order.setCreateTime(LocalDateTime.now());
                orderRepository.save(order);

                // 3. MQ 异步通知积分系统发货/加积分
                rocketMQTemplate.convertAndSend("seckill-event-topic", order);
                log.info("[Producer] Success event sent to RocketMQ for order: {}", order.getOrderNo());

                return "抢购成功！订单号为：" + order.getOrderNo();
            });

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "秒杀请求异常，请重新发起";
        } catch (Exception e) {
            // 捕获事务回滚时抛出的自定义 RuntimeException 并将其作为响应信息返还给前端
            return e.getMessage();
        } finally {
            // 确保事务完整 commit/rollback 后，才释放分布式锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}