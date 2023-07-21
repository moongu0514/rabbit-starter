package top.gtyun.starter.rabbitmq.core;

import com.rabbitmq.client.Channel;
import java.io.IOException;

import top.gtyun.starter.rabbitmq.listenter.MqListener;
import lombok.extern.slf4j.Slf4j;

/**
 * 手动确认rabbitmq消息处理器（手动确认消息，自动确认消息不需要继承这个类）
 *
 * @author gutao
 * @date 2023/07/21
 */
@Slf4j
public class BaseRabbiMqHandler<T> {

    public BaseRabbiMqHandler() {
    }

    public void onMessage(T t, Long deliveryTag, Channel channel, MqListener<T> mqListener) {
        try {
            mqListener.handler(t, channel);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("接收消息失败，重新放回队列:{}", e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException e1) {
                log.error(e1.getMessage());
            }
        }
    }
}

