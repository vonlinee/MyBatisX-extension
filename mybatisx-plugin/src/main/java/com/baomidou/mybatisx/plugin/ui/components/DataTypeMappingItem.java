package com.baomidou.mybatisx.plugin.ui.components;

import lombok.Getter;
import lombok.Setter;

import java.sql.JDBCType;

@Setter
@Getter
public class DataTypeMappingItem {

    private String group;
    private String identifier;
    private String anotherGroup;
    private String anotherIdentifier;

    /**
     * java数据类型，简单类型
     */
    private String javaType;

    /**
     * jdbc类型
     */
    private String jdbcType;

    /**
     * mysql类型
     */
    private String mysqlType;

    private String oracleType;

    public void setJdbcTypeEnum(JDBCType jdbcTypeEnum) {
        this.jdbcType = jdbcTypeEnum.getName();
    }
}
