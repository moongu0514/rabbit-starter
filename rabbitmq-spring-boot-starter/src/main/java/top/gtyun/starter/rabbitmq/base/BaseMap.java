package top.gtyun.starter.rabbitmq.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 基础MAP（简单消息封装）
 *
 * @author gutao
 * @date 2023/07/19
 */
public class BaseMap extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public BaseMap() {
    }

    public BaseMap(Map<String, Object> map) {
        this.putAll(map);
    }

    @Override
    public BaseMap put(String key, Object value) {
        super.put(key, Optional.ofNullable(value).orElse(""));
        return this;
    }

    public BaseMap add(String key, Object value) {
        super.put(key, Optional.ofNullable(value).orElse(""));
        return this;
    }

    public <T> T get(String key) {
        Object obj = super.get(key);
        return ObjectUtil.isNotEmpty(obj) ? (T) obj : null;
    }

    public Boolean getBoolean(String key) {
        Object obj = super.get(key);
        return ObjectUtil.isNotEmpty(obj) && Boolean.parseBoolean(obj.toString());
    }

    public Long getLong(String key) {
        Object v = this.get(key);
        return ObjectUtil.isNotEmpty(v) ? new Long(v.toString()) : null;
    }

    public Long[] getLongs(String key) {
        Object v = this.get(key);
        return ObjectUtil.isNotEmpty(v) ? (Long[]) ((Long[]) v) : null;
    }

    public List<Long> getListLong(String key) {
        List<Long> list = this.get(key);
        return ObjectUtil.isNotEmpty(list) ? list.stream().map(Long::new).collect(Collectors.toList()) : null;
    }

    public Long[] getLongIds(String key) {
        Object ids = this.get(key);
        return ObjectUtil.isNotEmpty(ids) ? Convert.toLongArray(ids.toString().split(",")) : null;
    }

    public Integer getInt(String key, Integer def) {
        Object v = this.get(key);
        return ObjectUtil.isNotEmpty(v) ? Integer.parseInt(v.toString()) : def;
    }

    public Integer getInt(String key) {
        Object v = this.get(key);
        return ObjectUtil.isNotEmpty(v) ? Integer.parseInt(v.toString()) : 0;
    }

    public BigDecimal getBigDecimal(String key) {
        Object v = this.get(key);
        return ObjectUtil.isNotEmpty(v) ? new BigDecimal(v.toString()) : new BigDecimal("0");
    }

    public <T> T get(String key, T def) {
        Object obj = super.get(key);
        return ObjectUtil.isEmpty(obj) ? def : (T) obj;
    }

    public static BaseMap toBaseMap(Map<String, Object> obj) {
        BaseMap map = new BaseMap();
        map.putAll(obj);
        return map;
    }
}
