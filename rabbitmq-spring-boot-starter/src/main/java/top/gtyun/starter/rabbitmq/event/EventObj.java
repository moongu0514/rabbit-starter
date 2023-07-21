package top.gtyun.starter.rabbitmq.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 事件对象
 *
 * @author gutao
 * @date 2023/07/21
 */
@Data
public class EventObj implements Serializable {
    private Object eventObj;
    private String handlerName;

}
