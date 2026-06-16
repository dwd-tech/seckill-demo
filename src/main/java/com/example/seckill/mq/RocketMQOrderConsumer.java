package com.example.seckill.mq;

import com.example.seckill.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RocketMQMessageListener(
        topic = "seckill-event-topic",
        consumerGroup = "seckill-consumer-group"
)
public class RocketMQOrderConsumer implements RocketMQListener<Order> {

    @Override
    public void onMessage(Order order) {
        log.info("[Consumer] Received Order Msg on MQ! Order No: {}", order.getOrderNo());
        // 模拟发放用户积分的业务延迟
        log.info("[Consumer] User: {} has been credits 100 points for securing product ID: {}",
                order.getUserId(), order.getGoodsId());
    }
}