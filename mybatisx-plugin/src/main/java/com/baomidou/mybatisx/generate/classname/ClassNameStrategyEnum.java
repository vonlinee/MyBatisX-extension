package com.baomidou.mybatisx.generate.classname;

public enum ClassNameStrategyEnum implements ClassNameStrategy {
    CAMEL("camel"),
    SAME("same as tablename");
    private final String text;

    ClassNameStrategyEnum(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String calculateClassName(String tableName, String ignorePrefix, String ignoreSuffix) {
        return tableName;
    }
}
