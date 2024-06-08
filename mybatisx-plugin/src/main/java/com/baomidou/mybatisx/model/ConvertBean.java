package com.baomidou.mybatisx.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConvertBean {
    private String sqlType;
    private String sqlTypeLength;

    public ConvertBean() {
    }

    public ConvertBean(String sqlType, String sqlTypeLength) {
        this.sqlType = sqlType;
        this.sqlTypeLength = sqlTypeLength;
    }
}
