package com.baomidou.mybatisx.feat.mybatis.generator.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
public class CustomTemplateRoot implements Serializable {

  private ModuleInfoGo moduleUIInfo;

  private DomainInfo domainInfo;

  private String templateText;
  private List<ModuleInfoGo> moduleInfoList = new ArrayList<>();

  public Map<? extends String, ?> toMap() {
    return moduleInfoList.stream().collect(Collectors.toMap(ModuleInfoGo::getConfigName, v -> v, (a, b) -> a));
  }
}
