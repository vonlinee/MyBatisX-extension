package com.baomidou.plugin.idea.mybatisx.model;

import java.util.regex.Pattern;

public class MsParam {
    static String L_BRACKET = "(";
    static String R_BRACKET = ")";
    static Pattern END_WITH_PAREN = Pattern.compile("\\((.*?)\\)$");
    private final String value;
    private final ParamDataType type;

    public MsParam(String value, ParamDataType type) {
        this.value = value;
        this.type = type;
    }

    public static MsParam of(String param) {
        if (!END_WITH_PAREN.matcher(param).find()) {
            return new MsParam(param.trim(), ParamDataType.STRING);
        }
        String value = param.substring(0, param.indexOf(L_BRACKET)).trim();
        ParamDataType type = ParamDataType.UNKNOWN;
        try {
            type = ParamDataType.valueOf(param.substring(param.indexOf(L_BRACKET) + 1, param.indexOf(R_BRACKET)).toUpperCase());
        } catch (IllegalArgumentException ignored) {

        }
        return new MsParam(value, type);
    }

    public String getValue() {
        return type.decorate(value);
    }
}
