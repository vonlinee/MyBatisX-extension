package com.baomidou.mybatisx.plugin.setting;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.plugin.ui.components.TemplateTreeViewNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplateGroup implements TemplateTreeViewNode {

  private String name;

  private List<TemplateSettingDTO> templates = new ArrayList<>();

  public void addTemplates(List<TemplateSettingDTO> templates) {
    this.templates.addAll(templates);
  }
}
