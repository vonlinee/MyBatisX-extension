package com.baomidou.mybatisx.model;

public abstract class AbstractComboBoxItem implements ComboBoxItem {

    private final String name;
    private final String value;

    public AbstractComboBoxItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
