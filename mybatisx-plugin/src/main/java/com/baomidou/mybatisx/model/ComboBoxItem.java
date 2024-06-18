package com.baomidou.mybatisx.model;

public interface ComboBoxItem {

    /**
     * 选项展示的文本
     *
     * @return 选项展示的文本
     */
    String getName();

    /**
     * 选项选中的值
     *
     * @return 选项选中的值
     */
    Object getValue();
}
