package hsja.ibas.iot.consumer;

import hsja.ibas.iot.consumer.config.RabbitConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.gtyun.starter.rabbitmq.client.RabbitClient;

import javax.annotation.Resource;

/**
 * @author gutao
 * @date 2023-07-28
 */
@SpringBootTest
public class Demo {

    @Resource
    private RabbitClient rabbitClient;
    @Resource
    private RabbitConfig rabbitConfig;

    @Test
    void testMqtt() throws JSONException {
//        rabbitClient.sendMessage("mqtt","test1231231");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","test").put("age",11);
        rabbitClient.sendMessageToExchange(rabbitConfig.iotExchange(),"mqtt", jsonObject);
    }

}
