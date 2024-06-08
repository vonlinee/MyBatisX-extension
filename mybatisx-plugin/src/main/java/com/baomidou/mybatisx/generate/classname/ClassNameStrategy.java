package com.baomidou.mybatisx.generate.classname;

public interface ClassNameStrategy {
    String getText();

    String calculateClassName(String tableName, String ignorePrefix, String ignoreSuffix);
}
