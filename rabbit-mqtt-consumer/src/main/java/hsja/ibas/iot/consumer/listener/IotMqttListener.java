package hsja.ibas.iot.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 设备句柄侦听器
 *
 * @author 10926
 * @date 2023/07/28
 */
@Slf4j
@Component
public class IotMqttListener {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "testIot", durable = "false", autoDelete = "true"),
                    exchange = @Exchange(value = "iot.exchange", type = "topic", durable = "false", autoDelete = "true"),
                    key = "mqtt"
            ))
    public void handleMessage(Message message) {
        System.out.println("Received message: " + message);
    }

}
