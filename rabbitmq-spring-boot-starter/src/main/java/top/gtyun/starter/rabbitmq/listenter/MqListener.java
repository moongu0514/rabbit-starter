package top.gtyun.starter.rabbitmq.listenter;

import com.rabbitmq.client.Channel;

/**
 * 监听器
 *
 * @author gutao
 * @date 2023/07/21
 */
public interface MqListener<T> {
    /**
     * 监听处理
     *
     * @param map 消息
     * @param channel 管道
     */
    void handler(T map, Channel channel);
}
