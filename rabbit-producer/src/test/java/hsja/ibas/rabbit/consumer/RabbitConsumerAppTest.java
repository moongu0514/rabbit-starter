package hsja.ibas.rabbit.consumer;

import top.gtyun.starter.rabbitmq.base.BaseMap;
import top.gtyun.starter.rabbitmq.client.RabbitClient;
import hsja.ibas.rabbit.producer.RabbitProducerApp;
import hsja.ibas.rabbit.producer.model.DeviceModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author gutao
 * @date 2023-07-13 14:13
 */
@SpringBootTest(classes = RabbitProducerApp.class)
public class RabbitConsumerAppTest {
    @Resource
    private RabbitClient rabbitClient;

    @Test
    void testWeb() {
        System.out.println("开始测试web ");
    }

    @Test
    void testProducer() {
        System.out.println("开始测试rabbit-producer");
        DeviceModel deviceMsg = DeviceModel.builder()
                .id(1000L)
                .name("one-测试设备")
                .type(1)
                .build();
        rabbitClient.sendMessage("test-device", deviceMsg);
    }

    @Test
    void testProducer4MultiMsg() {
        System.out.println("开始测试rabbit-producer produce multi-msg");
        DeviceModel deviceMsg = DeviceModel.builder()
                .id(123123L)
                .name("multi-测试设备1")
                .type(1)
                .build();
        DeviceModel deviceMsg2 = DeviceModel.builder()
                .id(123124L)
                .name("multi-测试设备2")
                .type(2)
                .build();
        DeviceModel deviceMsg3 = DeviceModel.builder()
                .id(123125L)
                .name("multi-测试设备3")
                .type(1)
                .build();
        rabbitClient.sendMessage("test-device", deviceMsg);
        rabbitClient.sendMessage("test-device", deviceMsg2);
        rabbitClient.sendMessage("test-device", deviceMsg3);
    }

    @Test
    void testEvent(){
        //rabbitmq消息总线测试
        BaseMap params = new BaseMap();
        params.put("id", 1L);
        params.put("name", "温湿度计");
        List<Integer> subIds = Arrays.asList(1, 2, 3);
        params.put("subIds", subIds);
        rabbitClient.publishEvent("demoBusEvent", params);

//        //rabbitmq消息总线测试
//        BaseMap params = new BaseMap();
//        params.put("orderId", "1");
//        rabbitClient.publishEvent("demoBusEvent", params);
//        params.put("orderName", "小吃");
//        rabbitClient.publishEvent("demoBusEvent", params);
//        params.put("orderCost", "$13");
//        rabbitClient.publishEvent("demoBusEvent", params);
    }
    @Test
    void testEventObj(){
        //rabbitmq消息总线测试
        List<Long> subIds = Arrays.asList(11L, 22L, 33L);
        DeviceModel device = DeviceModel.builder().id(1L).name("测试空压机1").type(11).subIds(subIds).build();
        rabbitClient.publishEvent("demoBusEvent", device);

//        //rabbitmq消息总线测试
//        BaseMap params = new BaseMap();
//        params.put("orderId", "1");
//        rabbitClient.publishEvent("demoBusEvent", params);
//        params.put("orderName", "小吃");
//        rabbitClient.publishEvent("demoBusEvent", params);
//        params.put("orderCost", "$13");
//        rabbitClient.publishEvent("demoBusEvent", params);
    }
    @Test
    void testFakeEvent(){
        //rabbitmq消息总线测试
        List<Long> subIds = Arrays.asList(11L, 22L, 33L);
        DeviceModel device = DeviceModel.builder().id(1L).name("测试空压机1").type(11).subIds(subIds).build();
        rabbitClient.publishEvent("demoBusEvent1", device);
    }

}
