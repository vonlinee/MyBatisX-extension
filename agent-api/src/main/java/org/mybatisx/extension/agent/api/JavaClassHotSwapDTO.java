package org.mybatisx.extension.agent.api;

import java.io.Serializable;

public class JavaClassHotSwapDTO implements Serializable {

    /**
     * java文件绝对路径
     */
    private String javaFilePath;

    public JavaClassHotSwapDTO(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }

    public String getJavaFilePath() {
        return javaFilePath;
    }

    public void setJavaFilePath(String javaFilePath) {
        this.javaFilePath = javaFilePath;
    }
}
