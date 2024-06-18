package com.baomidou.mybatisx.model;

/**
 * 数据类型集合
 */
public class GroupedDataTypeSet extends DataTypeSet {

    /**
     * 类型组ID，唯一，例如
     */
    private final String groupId;

    public GroupedDataTypeSet(String groupId) {
        this.groupId = groupId;
    }

    public final String getGroup() {
        return groupId;
    }
}
