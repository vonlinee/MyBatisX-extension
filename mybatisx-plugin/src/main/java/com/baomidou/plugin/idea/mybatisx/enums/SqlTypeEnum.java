package com.baomidou.plugin.idea.mybatisx.enums;

import com.baomidou.plugin.idea.mybatisx.util.StringUtils;

public enum SqlTypeEnum {

    TINYINT("TINYINT", false),
    INT("INT", false),
    BIGINT("BIGINT", false),
    FLOAT("FLOAT", true),
    DOUBLE("DOUBLE", true),
    DECIMAL("DECIMAL", false),
    DATE("DATE", true),
    DATETIME("DATETIME", true),
    TIMESTAMP("TIMESTAMP", true),
    CHAR("CHAR", false),
    VARCHAR("VARCHAR", false),
    TEXT("TEXT", true),
    LONGTEXT("LONGTEXT", true),
    TIME("TIME", true);

    private final String type;

    private final Boolean defaultLengthNeedEmpty;

    SqlTypeEnum(String type, Boolean defaultLengthNeedEmpty) {
        this.type = type;
        this.defaultLengthNeedEmpty = defaultLengthNeedEmpty;
    }

    public static SqlTypeEnum findByType(String type) {
        for (SqlTypeEnum typeEnum : values()) {
            if (StringUtils.equals(type, typeEnum.getType())) {
                return typeEnum;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public Boolean getDefaultLengthNeedEmpty() {
        return defaultLengthNeedEmpty;
    }
}
