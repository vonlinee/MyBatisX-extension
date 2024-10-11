package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.plugin.ui.components.DataType;
import com.baomidou.mybatisx.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 数据类型集合
 */
public class DataTypeSet extends HashSet<DataType> {

    @Override
    public boolean add(DataType s) {
        if (StringUtils.isEmpty(s.getIdentifier())) {
            return false;
        }
        return super.add(s);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends DataType> c) {
        return super.addAll(c);
    }

    @SafeVarargs
    public final <T extends DataType> boolean addAll(T... dataTypeIds) {
        return super.addAll(List.of(dataTypeIds));
    }
}
