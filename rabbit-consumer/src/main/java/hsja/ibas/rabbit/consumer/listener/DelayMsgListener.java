package hsja.ibas.rabbit.consumer.listener;

import com.rabbitmq.client.Channel;
import hsja.ibas.rabbit.consumer.model.DeviceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import top.gtyun.starter.rabbitmq.annotation.RabbitComponent;
import top.gtyun.starter.rabbitmq.core.BaseRabbiMqHandler;

/**
 * @author gutao
 * @date 2023-06-13 13:31
 */
@Slf4j
@RabbitComponent("test-delay-consumer")
public class DelayMsgListener extends BaseRabbiMqHandler<DeviceModel> {

    @RabbitListener(queues = "test-delay", ackMode = "MANUAL")//正常
//    @RabbitListener(queues = "test-device", ackMode = "NONE")//最后报错但是不会重复消费
//    @RabbitListener(queues = "test-device", ackMode = "AUTO")//报错且重复消费
//    @RabbitListener(queues = "test-device")// 报错且重复消费同AUTO
    public void onMessageManual(DeviceModel msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(msg, deliveryTag, channel, (msg1, channel1) -> {
            //业务处理
            log.info("测试 --> 延时消息：" + msg1);
        });
    }


////    @RabbitListener(queues = "test-device", ackMode = "MANUAL")//不报错但是队列中堆积消息
////    @RabbitListener(queues = "test-device", ackMode = "NONE")//？
////    @RabbitListener(queues = "test-device", ackMode = "AUTO")//正常
//    @RabbitListener(queues = "test-device")// 同AUTO
//    public void onMessageAuto(DeviceModel msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
//        log.info("查看唯一标识：{} ，信道：{}", deliveryTag, channel);
//        log.info("业务处理：{}，自动提交", msg);
//    }

}
