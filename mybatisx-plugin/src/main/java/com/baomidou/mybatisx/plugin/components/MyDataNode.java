package com.baomidou.mybatisx.plugin.components;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class MyDataNode {

  private String name;
  private String population;
  private String area;
  private String GDP;
  private String monsoonClimate;
  private List<MyDataNode> children;

  public MyDataNode(String name, String population, String area, String GDP, String monsoonClimate, List<MyDataNode> children) {
    this.name = name;
    this.population = population;
    this.area = area;
    this.GDP = GDP;
    this.monsoonClimate = monsoonClimate;
    this.children = children;
    if (this.children == null) {
      this.children = Collections.emptyList();
    }
  }

  public String toString() {
    return name;
  }
}
