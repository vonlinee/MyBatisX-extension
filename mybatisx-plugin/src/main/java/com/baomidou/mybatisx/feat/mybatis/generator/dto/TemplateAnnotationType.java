package com.baomidou.mybatisx.feat.mybatis.generator.dto;

import org.jetbrains.annotations.Nullable;

/**
 * 模板注解类型
 */
public enum TemplateAnnotationType {
  /**
   * 实体类没有注解
   */
  NONE,
  /**
   * 实体类加入 MYBATIS_PLUS3 注解
   */
  MYBATIS_PLUS3,
  /**
   * 实体类加入 MYBATIS_PLUS2 注解
   */
  MYBATIS_PLUS2,
  /**
   * 实体类加入 JPA 注解
   */
  JPA;

  @Nullable
  public static TemplateAnnotationType find(String name) {
    for (TemplateAnnotationType item : values()) {
      if (item.name().equals(name)) {
        return item;
      }
    }
    return null;
  }
}
