package com.baomidou.mybatisx.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ComboBoxItem {

    protected String name;

    protected String value;

    public ComboBoxItem() {
    }

    public ComboBoxItem(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
