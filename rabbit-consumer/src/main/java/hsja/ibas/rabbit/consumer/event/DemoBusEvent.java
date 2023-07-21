package hsja.ibas.rabbit.consumer.event;

import top.gtyun.starter.rabbitmq.event.IbasBusEventHandler;
import hsja.ibas.rabbit.consumer.model.DeviceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 演示总线事件
 *
 * @author 10926
 * @date 2023/07/19
 */
@Slf4j
@Component("demoBusEvent")
public class DemoBusEvent extends IbasBusEventHandler<DeviceModel> {

    @Override
    public void handle(DeviceModel deviceModel) {
        log.info("业务处理，已经转换好的javabean：{}", deviceModel);
//
//        if (ObjectUtil.isNotEmpty(eventObj)) {
//            Object obj = eventObj.getEventObj();
//            ObjectMapper objectMapper = new ObjectMapper();
//            BaseMap baseMap = objectMapper.convertValue(obj, BaseMap.class);
//            String name = baseMap.get("name");
//            System.out.println("name = " + name);
////            Object obj = eventObj.getEventObj();
////            ObjectMapper objectMapper = new ObjectMapper();
////            DeviceModel deviceModel = objectMapper.convertValue(obj, DeviceModel.class);
////            log.info("device:{}",deviceModel);
////            BaseMap baseMap = obj.getBaseMap();
////            String orderId = baseMap.get("orderId");
////            log.info("业务处理----订单ID:" + orderId);
//        }
    }
}
