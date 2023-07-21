package top.gtyun.starter.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件
 *
 * @author gutao
 * @date 2023-06-12 18:09
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "hsja.env.rabbit")
public class RabbitMqProperty {
    private String host;
    private int port;
    private String username;
    private String password;
    private String virtualHost;
}
