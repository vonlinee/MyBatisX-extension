package com.baomidou.mybatisx.feat.generate;

import com.baomidou.mybatisx.feat.generate.NamingStrategy;
import com.baomidou.mybatisx.util.StringUtils;

public enum DefaultNamingStrategy implements NamingStrategy {

    /**
     * 驼峰
     */
    CAMEL("camel") {
        @Override
        public String getText() {
            return "camel";
        }

        @Override
        public String apply(String src, String ignorePrefix, String ignoreSuffix) {
            String fName = src;
            final String EMPTY = "";
            if (!StringUtils.isEmpty(ignorePrefix)) {
                String[] prefixItems = ignorePrefix.split(",");
                for (String prefixItem : prefixItems) {
                    fName = fName.replaceAll("^" + prefixItem, EMPTY);
                }
            }
            if (!StringUtils.isEmpty(ignoreSuffix)) {
                String[] suffixItems = ignoreSuffix.split(",");
                for (String suffixItem : suffixItems) {
                    fName = fName.replaceFirst(suffixItem + "$", EMPTY);
                }
            }
            return StringUtils.dbStringToCamelStyle(fName);
        }
    },
    SAME("same as tablename") {
        @Override
        public String getText() {
            return "same as tablename";
        }

        @Override
        public String apply(String src, String ignorePrefix, String ignoreSuffix) {
            return src;
        }
    };
    private final String text;

    DefaultNamingStrategy(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String apply(String src, String ignorePrefix, String ignoreSuffix) {
        return src;
    }
}
