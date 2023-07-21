package top.gtyun.starter.rabbitmq.config;

import top.gtyun.starter.rabbitmq.event.IbasRemoteApplicationEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import javax.annotation.Resource;

/**
 * 配置
 *
 * @author gutao
 * @date 2023-06-12 16:32
 */
@Configuration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@EnableConfigurationProperties(RabbitMqProperty.class)
@RemoteApplicationEventScan(basePackageClasses = IbasRemoteApplicationEvent.class)
public class RabbitConfig implements RabbitListenerConfigurer {
    @Resource
    private RabbitMqProperty rabbitMqProperty;

    public RabbitConfig() {
    }

//    @Bean
//    @ConditionalOnBean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    @ConditionalOnBean
//    public ConnectionFactory connectionFactory() {
//        return this.getConnectionFactory();
//    }
//
//    public CachingConnectionFactory getConnectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(rabbitMqProperty.getHost());
//        connectionFactory.setPort(rabbitMqProperty.getPort());
//        connectionFactory.setUsername(rabbitMqProperty.getUsername());
//        connectionFactory.setPassword(rabbitMqProperty.getPassword());
//        connectionFactory.setVirtualHost(rabbitMqProperty.getVirtualHost());
//        return connectionFactory;
//    }
//
//    /**
//     * 用于创建默认的消费者容器工厂
//     *
//     * @param connectionFactory 连接工厂
//     * @return 消费者容器工厂
//     */
//    @Bean
//    @ConditionalOnBean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        // 设置并发消费数量1
//        factory.setConcurrentConsumers(1);
//        // 设置最大并发消费数量1
//        factory.setMaxConcurrentConsumers(1);
//        // 自动回复
//        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        // 指定一个consumer一次可以从Rabbit中获取多少条message并缓存在client中
//        factory.setPrefetchCount(1);
//        return factory;
//    }

    /**
     * rabbit管理器
     *
     * @param connectionFactory 连接工厂
     * @return rabbit管理器
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        return rabbitAdmin;
    }

//    /**
//     * 简单消息监听容器（可以在应用中动态设置）
//     *
//     * 有监听单个或多个队列、自动启动、自动声明功能。
//     * 可以设置事务特性、事务管理器、事务属性、事务并发、是否开启事务、回滚消息等。但是我们在实际生产中，很少使用事务，基本都是采用补偿机制。
//     * 可以设置消费者数量、最小最大数量、批量消费。
//     * 可以设置消息确认和自动确认模式、是否重回队列、异常捕获 Handler 函数。
//     * 可以设置消费者标签生成策略、是否独占模式、消费者属性等。
//     * 还可以设置具体的监听器、消息转换器等等。
//     *
//     * @param connectionFactory 连接工厂
//     * @return 简单消息监听容器
//     */
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        // 签收模式:NONE\AUTO\MANUAL
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        // 当前并发消费者数量
//        container.setConcurrentConsumers(1);
//        // 最大并发消费者数量
//        container.setMaxConcurrentConsumers(1);
//        // 消费者每次监听时可以拉取的消息数量
//        container.setPrefetchCount(1);
//        // 收到nack/reject确认信息时的处理方式，是否重新放回队列。true=扔回queue头部，false=丢弃
//        container.setDefaultRequeueRejected(true);
//        // 消费端的标签策略
//        container.setConsumerTagStrategy(queue -> queue + "_" + UUID.randomUUID().toString());
//        return container;
//    }

    /**
     * 错误消息处理器
     *
     * @return {@link RabbitListenerErrorHandler}
     */
    @Bean
    public RabbitListenerErrorHandler rabbitListenerErrorHandler() {
        return (amqpMessage, message, exception) -> {
            exception.printStackTrace();
            throw exception;
        };
    }

    /**
     * 兔子配置监听器
     *
     * @param registrar 注册商
     */
    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory());
    }

    /**
     * 消息处理程序方法工厂
     *
     * @return {@link MessageHandlerMethodFactory}
     */
    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(this.consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }

    /**
     * 使用者jackson2消息转换器
     *
     * @return {@link MappingJackson2MessageConverter}
     */
    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        // jackson转换器
        return new MappingJackson2MessageConverter();
    }

}

