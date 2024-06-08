package com.baomidou.mybatisx.feat.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BeanInfo {

    private String name;

    private List<Field> fields;

    public BeanInfo() {
    }

    public BeanInfo(String name, List<Field> fields) {
        this.name = name;
        this.fields = fields;
    }
}
