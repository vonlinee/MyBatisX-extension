package org.mybatisx.extension.agent.api;

public enum AgentCommandEnum {

    /**
     * mybatis mapper 文件热更新
     */
    MYBATIS_MAPPER_FILE_HOTSWAP,

    /**
     * mybatis mapper 单个语句热更新
     */
    MYBATIS_MAPPER_STATEMENT_HOTSWAP,

    /**
     * class热更新
     */
    JAVA_CLASS_HOTSWAP,
}
