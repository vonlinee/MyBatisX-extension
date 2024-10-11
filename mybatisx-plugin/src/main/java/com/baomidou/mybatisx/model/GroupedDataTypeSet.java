package com.baomidou.mybatisx.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * 数据类型集合
 */
public class GroupedDataTypeSet extends DataTypeSet {

    /**
     * 类型组ID，唯一，例如
     */
    @NotNull
    private final String groupId;

    public GroupedDataTypeSet(String groupId) {
        this.groupId = Objects.requireNonNull(groupId, "groupId cannot be null");
    }

    @NotNull
    public final String getGroupId() {
        return groupId;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof GroupedDataTypeSet && Objects.equals(((GroupedDataTypeSet) o).groupId, this.groupId);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.groupId);
    }
}
