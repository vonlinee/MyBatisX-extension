package com.baomidou.mybatisx.feat.generate.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模板上下文配置
 */
@Setter
@Getter
public class TemplateContext {
  /**
   * 项目路径
   */
  private String projectPath;
  /**
   * 模块名称
   */
  private String moduleName;
  /**
   * 注解类型
   */
  private String annotationType;
  /**
   * 模板名称
   */
  private String templateName;
  /**
   * 扩展的自定义模板
   */
  private Map<String, List<TemplateSettingDTO>> templateSettingMap;
  /**
   * 生成配置
   */
  private GenerateConfig generateConfig;
}
