package com.baomidou.mybatisx.feat.bean;

import lombok.Data;

import java.util.List;

@Data
public class IndexInfo {
    private String name;
    private String type;
    private String comment;
    private List<String> columns;

    public IndexInfo(String name, String type, String comment, List<String> columns) {
        this.name = name;
        this.type = type;
        this.comment = comment;
        this.columns = columns;
    }
}
