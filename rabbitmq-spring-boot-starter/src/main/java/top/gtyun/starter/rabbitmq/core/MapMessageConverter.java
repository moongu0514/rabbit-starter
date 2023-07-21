package top.gtyun.starter.rabbitmq.core;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 映射消息转换器
 *
 * @author gutao
 * @date 2023/07/21
 */
public class MapMessageConverter implements MessageConverter {
    public static final String TYPE_TEXT = "text";
    public MapMessageConverter() {
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(object.toString().getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String contentType = message.getMessageProperties().getContentType();
        if (null != contentType && contentType.contains(TYPE_TEXT)) {
            return new String(message.getBody());
        } else {
            ObjectInputStream objInt = null;

            try {
                ByteArrayInputStream byteInt = new ByteArrayInputStream(message.getBody());
                objInt = new ObjectInputStream(byteInt);
                Map map = (HashMap)objInt.readObject();
                return map;
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }
}
