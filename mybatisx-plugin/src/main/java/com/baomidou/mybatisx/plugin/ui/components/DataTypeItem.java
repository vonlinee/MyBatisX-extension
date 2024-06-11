package com.baomidou.mybatisx.plugin.ui.components;

import lombok.Data;

@Data
public class DataTypeItem {

    /**
     * 类型组唯一ID
     */
    private String groupId;

    /**
     * 单个组内唯一
     */
    private String identifier;

    public DataTypeItem() {
    }

    public DataTypeItem(String groupId) {
        this.groupId = groupId;
    }

    public DataTypeItem(String groupId, String identifier) {
        this.groupId = groupId;
        this.identifier = identifier;
    }
}
