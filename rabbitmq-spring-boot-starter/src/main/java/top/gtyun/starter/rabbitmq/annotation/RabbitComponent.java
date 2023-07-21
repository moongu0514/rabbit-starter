package top.gtyun.starter.rabbitmq.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * rabbit组件注解
 * 用于项目初始化时处理绑定关系
 *
 * @author gutao
 * @date 2023/07/21
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RabbitComponent {
    @AliasFor(
            annotation = Component.class
    )
    String value();
}
