package com.baomidou.plugin.idea.mybatisx.util;

import com.baomidou.plugin.idea.mybatisx.actions.Bean2DDLAction;
import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author breezes_y@163.com
 * @date 2021/2/10 23:15
 * @description 数据库类型和java类型映射工具类
 */
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

    public static class ConvertBean {
        private String sqlType;
        private String sqlTypeLength;

        public ConvertBean() {
        }

        public ConvertBean(String sqlType, String sqlTypeLength) {
            this.sqlType = sqlType;
            this.sqlTypeLength = sqlTypeLength;
        }

        public String getSqlType() {
            return sqlType;
        }

        public void setSqlType(String sqlType) {
            this.sqlType = sqlType;
        }

        public String getSqlTypeLength() {
            return sqlTypeLength;
        }

        public void setSqlTypeLength(String sqlTypeLength) {
            this.sqlTypeLength = sqlTypeLength;
        }
    }
}
