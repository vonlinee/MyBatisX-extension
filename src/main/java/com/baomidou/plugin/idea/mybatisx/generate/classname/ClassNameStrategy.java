package com.baomidou.plugin.idea.mybatisx.generate.classname;

public interface ClassNameStrategy {
    String getText();

    String calculateClassName(String tableName, String ignorePrefix, String ignoreSuffix);

    public enum ClassNameStrategyEnum {
        CAMEL("camel"),
        SAME("same as tablename");
        private String text;

        ClassNameStrategyEnum(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
