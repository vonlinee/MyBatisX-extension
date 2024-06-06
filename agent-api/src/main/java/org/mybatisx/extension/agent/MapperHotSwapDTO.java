package org.mybatisx.extension.agent;

import java.io.Serializable;

public class MapperHotSwapDTO implements Serializable {

    /**
     * 类全名称
     */
    private String mapperClass;

    /**
     * xml文件路径
     */
    private String mapperXmlPath;

    /**
     * Mapper 文件命名空间
     */
    private String namespace;

    /**
     * 标签内容
     */
    private String content;

    public String getMapperXmlPath() {
        return mapperXmlPath;
    }

    public void setMapperXmlPath(String mapperXmlPath) {
        this.mapperXmlPath = mapperXmlPath;
    }

    public String getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(String mapperClass) {
        this.mapperClass = mapperClass;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
