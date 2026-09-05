package com.baomidou.mybatisx.feat.mybatis.generator.dto;

import com.baomidou.mybatisx.plugin.ui.components.TemplateTreeViewNode;
import lombok.Data;

@Data
public class TemplateSettingDTO implements TemplateTreeViewNode {
  /**
   * 配置名称
   */
  private String configName;
  /**
   * 配置文件名称
   */
  private String configFile;
  /**
   * 文件名
   */
  private String fileName;
  /**
   * 后缀
   */
  private String suffix;
  /**
   * 包名
   */
  private String packageName;
  /**
   * 编码
   */
  private String encoding;
  /**
   * 模板内容
   */
  private String templateText;
  /**
   * 相对模块的资源文件路径
   */
  private String basePath;

  @Override
  public String getName() {
    return configFile;
  }
}
