package com.baomidou.plugin.idea.mybatisx.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TemplateInfo {

    /**
     * 模板分组
     */
    private String groupId;
    /**
     * 模板分组名称
     */
    private String groupName;
    /**
     * 模板ID
     */
    private String id;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板路径
     */
    private String path;
    /**
     * 模板内容
     */
    private String content;

    /**
     * 持久化状态Bean必须要默认构造器
     */
    public TemplateInfo() {
    }

    public TemplateInfo(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }
}
