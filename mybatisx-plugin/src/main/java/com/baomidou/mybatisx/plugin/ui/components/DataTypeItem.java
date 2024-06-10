package com.baomidou.mybatisx.plugin.ui.components;

import lombok.Data;

@Data
public class DataTypeItem {

    private String group;
    private String identifier;

    public DataTypeItem() {
    }

    public DataTypeItem(String group) {
        this.group = group;
    }

    public DataTypeItem(String group, String identifier) {
        this.group = group;
        this.identifier = identifier;
    }
}
