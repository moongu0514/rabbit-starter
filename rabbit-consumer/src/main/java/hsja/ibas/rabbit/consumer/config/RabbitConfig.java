package hsja.ibas.rabbit.consumer.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author gutao
 * @date 2023-07-27
 */
public class RabbitConfig {

    /**
     * 用于创建默认的消费者容器工厂
     *
     * @param connectionFactory 连接工厂
     * @return 消费者容器工厂
     */
    @Bean("IotRabbitContainerFactory")
    @ConditionalOnBean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 设置并发消费数量1
        factory.setConcurrentConsumers(1);
        // 设置最大并发消费数量1
        factory.setMaxConcurrentConsumers(1);
        // 自动回复
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // 指定一个consumer一次可以从Rabbit中获取多少条message并缓存在client中
        factory.setPrefetchCount(1);
        return factory;
    }
}
