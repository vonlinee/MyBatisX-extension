package com.baomidou.mybatisx.mybatis;

import java.util.Arrays;

/**
 * MyBatis Mapper Statement 参数值数据类型
 */
public enum MSParamDataType {

    /**
     * NULL类型
     */
    NULL(-1, "Null"),
    /**
     * 布尔值
     */
    BOOLEAN(1, "Boolean"),
    /**
     * 数值类型
     */
    NUMERIC(2, "Numeric"),
    /**
     * 字符串
     */
    STRING(3, "String"),
    /**
     * 集合类型 元素类型
     */
    COLLECTION(4, "Collection");

    private final int type;
    private final String typeName;

    MSParamDataType(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public static MSParamDataType valueOfTypeName(String typeName) {
        if (typeName == null) {
            return null;
        }
        for (MSParamDataType item : values()) {
            if (item.getQualifier().equals(typeName)) {
                return item;
            }
        }
        return null;
    }

    public static MSParamDataType valueOfType(int type, MSParamDataType defaultValue) {
        return Arrays.stream(values()).filter(i -> i.getType() == type).findFirst().orElse(defaultValue);
    }

    public static MSParamDataType fromType(Class<?> javaType) {
        if (javaType == null) {
            return MSParamDataType.STRING;
        }
        if (Number.class.isAssignableFrom(javaType)) {
            return MSParamDataType.NUMERIC;
        }
        return MSParamDataType.STRING;
    }

    public final String getQualifier() {
        return typeName;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }
}
