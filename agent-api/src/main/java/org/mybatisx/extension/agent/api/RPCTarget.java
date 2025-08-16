package org.mybatisx.extension.agent.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RPCTarget {
    /**
     * 实现类名称
     *
     * @return Agent实现类名称
     * @see java.lang.Class#getSimpleName()
     * @see MyBatisXAgent
     */
    String value();

    /**
     * 目标ip
     *
     * @return 目标IP地址
     */
    String ip() default "127.0.0.1";

    /**
     * 目标端口
     *
     * @return 端口地址
     */
    int port() default 8080;
}
