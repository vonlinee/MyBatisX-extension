package com.baomidou.mybatisx.util;

import com.baomidou.mybatisx.plugin.actions.Bean2DDLAction;
import com.baomidou.mybatisx.model.ConvertBean;
import com.baomidou.mybatisx.plugin.setting.JavaBean2DDLSetting;

import java.util.concurrent.ConcurrentHashMap;

public class SqlTypeMapUtil {

    private static volatile SqlTypeMapUtil sqlTypeMapUtil;

    private SqlTypeMapUtil() {
    }

    public static SqlTypeMapUtil getInstance() {
        if (sqlTypeMapUtil == null) {
            synchronized (SqlTypeMapUtil.class) {
                if (sqlTypeMapUtil == null) {
                    sqlTypeMapUtil = new SqlTypeMapUtil();
                }
            }
        }
        return sqlTypeMapUtil;
    }

    public ConcurrentHashMap<String, ConvertBean> convertMapInit() {
        JavaBean2DDLSetting.MySettingProperties properties = JavaBean2DDLSetting.getInstance().myProperties;
        ConcurrentHashMap<String, ConvertBean> convertMap = new ConcurrentHashMap<>();
        convertMap.put("int", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("Integer", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("Long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("Double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("Float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Date", new ConvertBean(properties.getDateType(), properties.getDateDefaultLength()));
        convertMap.put("String", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("char", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("Character", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("BigDecimal", new ConvertBean(properties.getBigDecimalType(), properties.getBigDecimalDefaultLength()));
        convertMap.put("LocalDate", new ConvertBean(properties.getLocalDateType(), properties.getLocalDateDefaultLength()));
        convertMap.put("LocalTime", new ConvertBean(properties.getLocalTimeType(), properties.getLocalTimeDefaultLength()));
        convertMap.put("LocalDateTime", new ConvertBean(properties.getLocalDateTimeType(), properties.getLocalDateTimeDefaultLength()));
        return convertMap;
    }

    public ConvertBean typeConvert(String javaType) {
        if (StringUtils.isBlank(javaType)) {
            return null;
        }
        return Bean2DDLAction.convertMap.get(javaType);
    }
}
