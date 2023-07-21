package hsja.ibas.rabbit.producer.listener;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import java.util.Date;

/**
 * @author gutao
 * @date 2023-07-17 13:02
 */
public class DemoListener {

    public String getNextDay(String dateStr) {
        DateTime dateTime = DateUtil.parse(dateStr, "yyyy-MM-dd");
        Date date = dateTime.toJdkDate();
        DateTime dateTime1 = DateUtil.endOfDay(date);
        String result = dateTime1.toString();
        System.out.println("result = " + result);
        return result;
    }
}
