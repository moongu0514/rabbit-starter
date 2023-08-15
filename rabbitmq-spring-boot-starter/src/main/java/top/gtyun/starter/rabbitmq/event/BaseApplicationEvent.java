//package top.gtyun.starter.rabbitmq.event;
//
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
///**
// * 基础应用事件
// * 当前springboot 2.3.12.RELEASE不支持
// * 需要2.7.10及以上
// *
// * @author gutao
// * @date 2023/07/21
// */
//@Slf4j
//@Component
//@Deprecated
//public class BaseApplicationEvent implements ApplicationListener<IbasRemoteApplicationEvent> {
//    public BaseApplicationEvent() {
//    }
//
//    @Override
//    public void onApplicationEvent(IbasRemoteApplicationEvent event) {
//        log.info("ApplicationEvent:{}", event);
//        EventObj eventObj = event.getEvent();
//        if (ObjectUtil.isNotEmpty(eventObj)) {
//            IbasBusEventHandler busEventHandler = SpringUtil.getBean(eventObj.getHandlerName(), IbasBusEventHandler.class);
//            if (ObjectUtil.isNotEmpty(busEventHandler)) {
//                busEventHandler.onMessage(eventObj);
//            }
//        }
//    }
//
//}
