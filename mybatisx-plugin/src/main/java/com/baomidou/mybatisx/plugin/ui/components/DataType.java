package com.baomidou.mybatisx.plugin.ui.components;

import org.jetbrains.annotations.NotNull;

public interface DataType {
    @NotNull
    String getGroupIdentifier();

    @NotNull
    String getIdentifier();

    @NotNull
    String getName();
}
