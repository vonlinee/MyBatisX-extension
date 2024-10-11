package com.baomidou.mybatisx.plugin.ui.components;

public interface MultableDataType extends DataType {

    void setIdentifier(String identifier);

    void setTypeGroup(String typeGroup);

    void setName(String name);

    void setLength(int minLength, int maxLength);
}
