package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;

import java.util.List;

public class ParamNode {

  /**
   * 参数Key，嵌套形式，比如 user.name
   */
  private String key;

  /**
   * 字面值
   */
  private String value;

  /**
   * 数据类型
   */
  private ParamDataType dataType;

  private List<ParamNode> children;

  public ParamNode(String key, String value, ParamDataType dataType) {
    this.key = key;
    this.value = value;
    this.dataType = dataType;
  }

  @Override
  public String toString() {
    return key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public ParamDataType getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = ParamDataType.valueOf(dataType);
  }

  public void setDataType(ParamDataType dataType) {
    this.dataType = dataType;
  }

  public List<ParamNode> getChildren() {
    return children;
  }

  public void setChildren(List<ParamNode> children) {
    this.children = children;
  }
}
