package com.baomidou.mybatisx.feat.ddl;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableField {

    private boolean isPrimaryKey;

    private boolean isGeneratedValue;

    private String name;

    private String type;

    private Integer length;

    private boolean nullable = true;

    private String desc;

    private boolean hasTypeTranslate = false;

    public String getType() {
        if (type == null) {
            return "unknown";
        }
        return type;
    }

    public Integer getLength() {
        if (length == null) {
            if ("varchar".equals(this.getType())) {
                return 255;
            } else if ("int".equals(this.getType())) {
                return 8;
            } else if ("bigint".equals(this.getType())) {
                return 20;
            }
            return null;
        }
        return length;
    }
}
