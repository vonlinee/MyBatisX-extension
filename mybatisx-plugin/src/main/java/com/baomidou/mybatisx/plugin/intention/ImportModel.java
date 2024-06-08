package com.baomidou.mybatisx.plugin.intention;

public enum ImportModel {
    OVERRIDE(1, "全部覆盖"),
    APPEND(2, "仅追加"),
    MERGE(3, "合并不覆盖"),
    MERGE_OVERRIDE(4, "合并且覆盖");

    final int type;
    final String label;

    ImportModel(int type, String label) {
        this.type = type;
        this.label = label;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public int getType() {
        return this.type;
    }

    public String getLabel() {
        return this.label;
    }
}
