package hsja.ibas.iot.consumer.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.gtyun.starter.rabbitmq.client.RabbitClient;
import top.gtyun.starter.rabbitmq.exchange.ExchangeBuilder;

/**
 * 兔子配置
 *
 * @author 10926
 * @date 2023/07/28
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Bean
    public Queue myQueue() {
        log.info("创建自定义队列==testIot");
        return new Queue("testIot", false, false, true);
//        return new Queue("testIot");
    }

    @Bean
    public TopicExchange iotExchange() {
        return new TopicExchange("iot.exchange", false, true);
    }
//
//    @Bean
//    public Binding myBinding(Queue myQueue, RabbitClient rabbitClient) {
//        return BindingBuilder.bind(myQueue).to(rabbitClient.createExchange(ExchangeBuilder.DIRECT_EXCHANGE)).with("mqtt");
//    }

//    @Bean(ExchangeBuilder.DIRECT_EXCHANGE)
//    public Exchange myExchange(RabbitClient rabbitClient) {
//        log.info("创建自定义交换机=={}", ExchangeBuilder.DIRECT_EXCHANGE);
//        return rabbitClient.createExchange(ExchangeBuilder.DIRECT_EXCHANGE);
//    }

//    @Bean
//    public CustomExchange iotExchange() {
//        return new CustomExchange("iot.exchange", "direct", false, true);
//    }
}
