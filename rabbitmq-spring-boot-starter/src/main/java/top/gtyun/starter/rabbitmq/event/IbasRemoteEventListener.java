package top.gtyun.starter.rabbitmq.event;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bus.SpringCloudBusClient;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * 远程事件监听
 * 重写org.springframework.cloud.bus.BusAutoConfiguration.acceptRemote()
 *
 * @author gutao
 * @date 2023-07-14 10:12
 */
@Slf4j
@Component
public class IbasRemoteEventListener {

    @StreamListener(SpringCloudBusClient.INPUT)
    public void acceptRemote(RemoteApplicationEvent event) {
        // 接收远程事件，可能会收到自己发送的，暂时无法去除，需要在业务层判断
        if (log.isDebugEnabled()) {
            log.debug("订阅事件：{}", event);
        }
        if (!(event instanceof IbasRemoteApplicationEvent)) {
            log.error("非业务远程事件：{}", event);
            return;
        }
        // 获取事件内容
        EventObj eventObj = ((IbasRemoteApplicationEvent)event).getEvent();
        if (ObjectUtil.isEmpty(eventObj)) {
            if (log.isDebugEnabled()) {
                log.debug("事件内容为空:{}", event);
            }
            return;
        }
        // 上下文中获取事件处理器
        IbasBusEventHandler<?> busEventHandler = null;
        try {
            busEventHandler = SpringUtil.getBean(eventObj.getHandlerName(), IbasBusEventHandler.class);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("不存在对应的事件处理器：{}", e.getMessage());
            }
            return;
        }
        if (ObjectUtil.isEmpty(busEventHandler)) {
            if (log.isDebugEnabled()) {
                log.debug("不存在对应的事件处理器");
            }
            return;
        }
        // 调用事件处理器的方法
        if (log.isDebugEnabled()) {
            log.debug("调用事件处理器{}，处理事件", busEventHandler);
        }
        busEventHandler.onMessage(eventObj);
    }

}
