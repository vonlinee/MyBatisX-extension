package com.baomidou.mybatisx.feat.mybatis.generator.dto;

import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.StringUtils;
import lombok.Data;

import java.util.List;

@Data
public class GenerateConfig {

  /**
   * 忽略表的前缀
   */
  private String ignoreTablePrefix;
  /**
   * 忽略表的后缀
   */
  private String ignoreTableSuffix;

  /**
   * 界面恢复
   */
  private String moduleName;

  private String annotationType;

  /**
   * 基础包名
   */
  private String basePackage;
  /**
   * 相对包路径
   */
  private String relativePackage;
  /**
   * 编码方式, 默认: UTF-8
   */
  private String encoding;
  /**
   * 模块的源码相对路径
   */
  private String basePath;
  /**
   * 模块路径
   */
  private String modulePath;

  /**
   * 需要生成 toString,hashcode,equals
   */
  private boolean needToStringHashcodeEquals;
  /**
   * 需要生成实体类注释
   */
  private boolean needsComment;
  /**
   * 实体类需要继承的父类
   */
  private String superClass;
  /**
   * 需要移除的字段前缀
   */
  private String ignoreFieldPrefix;
  /**
   * 需要移除的字段后缀
   */
  private String ignoreFieldSuffix;

  /**
   * 需要生成repository注解
   *
   * @Repository
   */
  private boolean repositoryAnnotation;

  private boolean useLombokPlugin;
  private boolean useActualColumns;
  private boolean jsr310Support;
  private boolean useActualColumnAnnotationInject;
  /**
   * 模板组名称
   */
  private String templatesName;
  /**
   * 额外的类名后缀
   */
  private String extraClassSuffix;
  /**
   * 已选择的模板名称
   */
  private List<ModuleInfoGo> moduleUIInfoList;
  /**
   * 要生成的表信息列表
   */

  private transient List<TableUIInfo> tableUIInfoList;

  /**
   * 类名生成策略
   * CAMEL: 根据表名生成驼峰命名
   * SAME: 使用表明
   */
  private String classNameStrategy;

  public boolean checkGenerate() {
    if (StringUtils.isEmpty(moduleName)) {
      MessageNotification.showErrorDialog("Generate Info", "moduleName must not be empty");
      return false;
    }
    if (StringUtils.isEmpty(templatesName)) {
      MessageNotification.showErrorDialog("Generate Info", "templatesName must not be empty");
      return false;
    }
    return true;
  }
}
