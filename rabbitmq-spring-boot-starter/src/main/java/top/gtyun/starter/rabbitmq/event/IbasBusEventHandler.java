package top.gtyun.starter.rabbitmq.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;

/**
 * 事件处理抽象类
 *
 * @author gutao
 * @date 2023/07/21
 */
@Slf4j
public abstract class IbasBusEventHandler<T> {

    /**
     * 监听消息
     *
     * @param event 事件
     */
    public void onMessage(EventObj event) {
        Class<?> type = ResolvableType.forClass(getClass()).getSuperType().resolveGeneric(0);
        if (log.isDebugEnabled()) {
            log.debug("泛型类为：{}", type);
        }
        Object obj = event.getEventObj();
        ObjectMapper objectMapper = new ObjectMapper();
        T bizBean = null;
        try {
            bizBean = (T) objectMapper.convertValue(obj, type);
        } catch (IllegalArgumentException e) {
            log.error("类型转换异常，选择合适的泛型：{}", e.getMessage());
//            throw new RuntimeException("类型转换异常，选择合适的泛型");
            handleError(obj);
            return;
        }
        handle(bizBean);
    }

    public void handleError(Object obj) {
        log.error("do nothing");
    }

    /**
     * 处理事件
     *
     * @param obj 事件消息
     */
    public abstract void handle(T obj);
}
