package com.baomidou.mybatisx.feat.generate;

public interface NamingStrategy {

    /**
     * 展示的文本
     *
     * @return 展示的文本
     */
    String getText();

    /**
     * 应用命名策略
     *
     * @param src          原字符串
     * @param ignorePrefix 忽略前缀
     * @param ignoreSuffix 忽略后缀
     * @return 命名策略转换结果
     */
    String apply(String src, String ignorePrefix, String ignoreSuffix);
}
