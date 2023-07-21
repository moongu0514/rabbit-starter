package top.gtyun.starter.rabbitmq.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;


/**
 * 远程应用程序事件
 *
 * @author gutao
 * @date 2023/07/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IbasRemoteApplicationEvent extends RemoteApplicationEvent {
    private EventObj event;

    private IbasRemoteApplicationEvent() {
    }

    public IbasRemoteApplicationEvent(EventObj source, String originService, String destinationService) {
        super(source, originService, destinationService);
        this.event = source;
    }

    public IbasRemoteApplicationEvent(EventObj source, String originService) {
        super(source, originService, "");
        this.event = source;
    }

}
