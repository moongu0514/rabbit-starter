package hsja.ibas.iot.consumer.listener;

import hsja.ibas.iot.consumer.model.DeviceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import top.gtyun.starter.rabbitmq.core.BaseRabbiMqHandler;
import com.rabbitmq.client.Channel;

/**
 * 设备句柄侦听器
 *
 * @author 10926
 * @date 2023/07/28
 */
@Slf4j
//@RabbitComponent("iot-consumer")
public class IotHandleListener extends BaseRabbiMqHandler<DeviceModel> {

    /**
     * 对消息汽车
     *
     * @param msg         味精
     * @param channel     通道
     * @param deliveryTag 交货标签
     */
    @RabbitListener(queues = "mqtt")
    public void onMessageAuto(DeviceModel msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("查看唯一标识：{} ，信道：{}", deliveryTag, channel);
        log.info("业务处理：{}，自动提交", msg);
    }

}
