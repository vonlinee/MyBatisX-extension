package com.baomidou.mybatisx.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * 数据类型集合
 */
public class DataTypeSet extends HashSet<String> {

    @Override
    public boolean add(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return super.add(s);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends String> c) {
        return super.addAll(c);
    }

    @SafeVarargs
    public final <T extends String> boolean addAll(T... dataTypeIds) {
        return super.addAll(List.of(dataTypeIds));
    }
}
