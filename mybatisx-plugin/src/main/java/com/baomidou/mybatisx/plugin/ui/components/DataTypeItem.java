package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.ComboBoxItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataTypeItem implements ComboBoxItem {

    /**
     * 类型组唯一ID
     */
    private String groupId;

    /**
     * 单个组内唯一
     */
    private String identifier;

    public DataTypeItem(String groupId) {
        this.groupId = groupId;
    }

    public DataTypeItem(String groupId, String identifier) {
        this.groupId = groupId;
        this.identifier = identifier;
    }

    @Override
    public String getName() {
        return groupId;
    }

    @Override
    public Object getValue() {
        return groupId;
    }
}
