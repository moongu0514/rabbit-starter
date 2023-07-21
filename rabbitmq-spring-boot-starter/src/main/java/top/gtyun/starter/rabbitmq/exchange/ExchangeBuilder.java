package top.gtyun.starter.rabbitmq.exchange;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.CustomExchange;


/**
 * 交换机构建器
 *
 * @author gutao
 * @date 2023/07/19
 */
public class ExchangeBuilder {
    public static final String DEFAULT_DELAY_EXCHANGE = "ibas.delayed.exchange";
    public static final String DIRECT_EXCHANGE = "ibas.direct.exchange";

    public ExchangeBuilder() {
    }

    /**
     * 构建延时交换机
     *
     * @return 自定义的交换机
     */
    public static CustomExchange buildDelayExchange() {
        Map<String, Object> args = new HashMap<>(16);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DEFAULT_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }
}
