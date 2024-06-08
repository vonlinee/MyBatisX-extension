package com.baomidou.plugin.idea.mybatisx.model;

import com.baomidou.plugin.idea.mybatisx.ddl.SqlTypeAndJavaTypeEnum;
import com.baomidou.plugin.idea.mybatisx.util.SqlTypeMapUtil;
import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Field {

    public String name;

    private String type;

    private Boolean primaryKey;

    private String comment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static Field newField(String name, String type, boolean primaryKey, String comment) {
        return new Field(name, type, primaryKey, comment);
    }

    public static Field newField(String name, String type) {
        return new Field(name, type);
    }

    public String getTableColumn() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name);
    }

    public String getSqlType() {
        SqlTypeMapUtil.ConvertBean convertBean = SqlTypeMapUtil.getInstance().typeConvert(this.type);
        if (null != convertBean) {
            return convertBean.getSqlType() + convertBean.getSqlTypeLength();
        }
        /*兜底配置*/
        return getSqlTypeForMapping() + getSqlTypeSize();
    }

    /**
     * 获取mysql类型
     */
    public String getSqlTypeForMapping() {
        return SqlTypeAndJavaTypeEnum.findByJavaType(this.type).getSqlType();
    }

    public String getSqlTypeSize() {
        return SqlTypeAndJavaTypeEnum.findByJavaType(this.type).getDefaultLength();
    }

    public Field() {
    }

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
    }

    public Field(String name, String type, Boolean primaryKey, String comment) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.comment = comment;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }
}
