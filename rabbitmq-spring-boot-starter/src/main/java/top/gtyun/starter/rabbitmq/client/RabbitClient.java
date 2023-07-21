package top.gtyun.starter.rabbitmq.client;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import top.gtyun.starter.rabbitmq.annotation.RabbitComponent;
import top.gtyun.starter.rabbitmq.event.EventObj;
import top.gtyun.starter.rabbitmq.event.IbasRemoteApplicationEvent;
import top.gtyun.starter.rabbitmq.exchange.ExchangeBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * rabbitmq客户端工具，收发消息
 *
 * @author gutao
 * @date 2023/07/21
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitClient {
    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final BusProperties busProperties;
    private final ApplicationEventPublisher publisher;
    private final ApplicationContext applicationContext;
    /**
     * 简单的消息体
     */
    private final Map<String, Object> sentObj = new HashMap<>(16);

    @PostConstruct
    public void initQueue() {
        Map<String, Object> beansWithRabbitComponentMap = this.applicationContext.getBeansWithAnnotation(RabbitComponent.class);
        Class<?> clazz;

        for (Entry<String, Object> stringObjectEntry : beansWithRabbitComponentMap.entrySet()) {
            clazz = stringObjectEntry.getValue().getClass();
            Method[] methods = clazz.getMethods();
            RabbitListener rabbitListener = clazz.getAnnotation(RabbitListener.class);
            if (ObjectUtil.isNotEmpty(rabbitListener)) {
                log.info("初始化队列....class........rabbitListener={}", rabbitListener);
                this.createQueue(rabbitListener);
            }

            for (Method method : methods) {
                RabbitListener methodRabbitListener = method.getAnnotation(RabbitListener.class);
                if (ObjectUtil.isNotEmpty(methodRabbitListener)) {
                    log.info("初始化队列...method.........methodRabbitListener={}", methodRabbitListener);
                    this.createQueue(methodRabbitListener);
                }
            }
        }

    }

    /**
     * 基于监听注解创建队列（队列名和routing-key一致）
     *
     * @param rabbitListener 监听注解
     */
    private void createQueue(RabbitListener rabbitListener) {
        String[] queues = rabbitListener.queues();
        DirectExchange directExchange = this.createExchange(ExchangeBuilder.DIRECT_EXCHANGE);
        this.rabbitAdmin.declareExchange(directExchange);
        if (ObjectUtil.isNotEmpty(queues)) {

            for (String queueName : queues) {
                Properties result = this.rabbitAdmin.getQueueProperties(queueName);
                if (ObjectUtil.isEmpty(result)) {
                    Queue queue = new Queue(queueName);
                    this.addQueue(queue);
                    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
                    this.rabbitAdmin.declareBinding(binding);
                    log.info("创建队列:{}", queueName);
                } else {
                    log.info("已有队列:{}", queueName);
                }
            }
        }

    }

    /**
     * 基于队列名创建队列（队列名和routing-key一致）
     *
     * @param queueName 队列名称
     * @return 是否成功
     */
    public boolean createQueue(String queueName) {
        DirectExchange directExchange = this.createExchange(ExchangeBuilder.DIRECT_EXCHANGE);
        this.rabbitAdmin.declareExchange(directExchange);
        Properties result = this.rabbitAdmin.getQueueProperties(queueName);
        if (ObjectUtil.isEmpty(result)) {
            Queue queue = new Queue(queueName);
            this.addQueue(queue);
            Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
            this.rabbitAdmin.declareBinding(binding);
            log.info("创建队列:{}", queueName);
            return true;
        } else {
            log.info("已有队列:{}", queueName);
            return false;
        }
    }

    /**
     * 发送普通的消息
     * 默认交换机（队列名和routing-key一致）
     *
     * @param queueName 队列名
     * @param msg 消息
     */
    public void sendMessage(String queueName, Object msg) {
        if (log.isDebugEnabled()) {
            log.debug("发送消息到默认交换机:{},队列名和routing-key:{},参数:{}", ExchangeBuilder.DIRECT_EXCHANGE, queueName, msg);
        }
        try {
            this.rabbitTemplate.convertAndSend(ExchangeBuilder.DIRECT_EXCHANGE, queueName, msg, (message) -> message);
        } catch (Exception e) {
            log.error("rabbitmq发送消息失败：{}", e.getMessage());
        }

    }

    /**
     * 推送远程事件
     *
     * @param handlerName 事件处理器名称
     * @param eventMsg 消息
     */
    public void publishEvent(String handlerName, Object eventMsg) {
        EventObj eventObj = new EventObj();
        eventObj.setHandlerName(handlerName);
        eventObj.setEventObj(eventMsg);
        this.publisher.publishEvent(new IbasRemoteApplicationEvent(eventObj, this.busProperties.getId()));
    }

    /**
     * 消息类型转换
     *
     * @param messageType 消息类型
     * @param msg 消息
     * @return Message类型的消息
     */
    public Message getMessage(String messageType, Object msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(messageType);
        Message message = new Message(msg.toString().getBytes(), messageProperties);
        return message;
    }

    public void sendMessageToExchange(TopicExchange topicExchange, String routingKey, Object msg) {
        Message message = this.getMessage("application/json", msg);
        this.rabbitTemplate.send(topicExchange.getName(), routingKey, message);
    }

    public void sendMessageToExchange(TopicExchange topicExchange, AbstractExchange exchange, String msg) {
        this.addExchange(exchange);
        if (log.isDebugEnabled()) {
            log.debug("RabbitMQ send {} -> {}",exchange.getName(), msg);
        }
        this.rabbitTemplate.convertAndSend(topicExchange.getName(), msg);
    }

    public void sendMessage(String queueName) {
        this.send(queueName, this.sentObj, 0);
        this.sentObj.clear();
    }

    public RabbitClient put(String key, Object value) {
        this.sentObj.put(key, value);
        return this;
    }

    public void sendMessage(String queueName, Object params, Integer expiration) {
        this.send(queueName, params, expiration);
    }

    /**
     * 延时消息，需要rabbit安装延时插件
     *
     * @param queueName 队列名
     * @param params 消息
     * @param expiration 延时时间
     */
    private void send(String queueName, Object params, Integer expiration) {
        Queue queue = new Queue(queueName);
        this.addQueue(queue);
        CustomExchange customExchange = ExchangeBuilder.buildDelayExchange();
        this.rabbitAdmin.declareExchange(customExchange);
        Binding binding = BindingBuilder.bind(queue).to(customExchange).with(queueName).noargs();
        this.rabbitAdmin.declareBinding(binding);
        if (log.isDebugEnabled()) {
            log.debug("延时任务-发送时间：{}", DateUtil.now());
        }
        this.rabbitTemplate.convertAndSend(ExchangeBuilder.DEFAULT_DELAY_EXCHANGE, queueName, params, (message) -> {
            if (expiration != null && expiration > 0) {
                message.getMessageProperties().setHeader("x-delay", expiration);
            }

            return message;
        });
    }

    public String receiveFromQueue(String queueName) {
        return this.receiveFromQueue(DirectExchange.DEFAULT, queueName);
    }

    public String receiveFromQueue(DirectExchange directExchange, String queueName) {
        Queue queue = new Queue(queueName);
        this.addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        this.rabbitAdmin.declareBinding(binding);
        String messages = (String) this.rabbitTemplate.receiveAndConvert(queueName);
        if (log.isDebugEnabled()) {
            log.debug("receiveFromQueue,msg:{}", messages);
        }
        return messages;
    }

    public void addExchange(AbstractExchange exchange) {
        this.rabbitAdmin.declareExchange(exchange);
    }

    public boolean deleteExchange(String exchangeName) {
        return this.rabbitAdmin.deleteExchange(exchangeName);
    }

    public Queue addQueue() {
        return this.rabbitAdmin.declareQueue();
    }

    public String addQueue(Queue queue) {
        return this.rabbitAdmin.declareQueue(queue);
    }

    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        this.rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    public boolean deleteQueue(String queueName) {
        return this.rabbitAdmin.deleteQueue(queueName);
    }

    public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        this.rabbitAdmin.declareBinding(binding);
    }

    public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
        Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
        this.rabbitAdmin.declareBinding(binding);
    }

    public void removeBinding(Binding binding) {
        this.rabbitAdmin.removeBinding(binding);
    }

    public DirectExchange createExchange(String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }
}
