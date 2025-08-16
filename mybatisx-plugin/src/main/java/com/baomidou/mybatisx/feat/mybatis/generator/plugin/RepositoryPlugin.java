package com.baomidou.mybatisx.feat.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;

import java.util.List;

public class RepositoryPlugin extends PluginAdapter {
  private final FullyQualifiedJavaType annotationRepository =
    new FullyQualifiedJavaType("org.springframework.stereotype.Repository");

  public RepositoryPlugin() {
  }

  @Override
  public boolean validate(List<String> list) {
    return true;
  }

  @Override
  public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
    interfaze.addImportedType(this.annotationRepository);
    interfaze.addAnnotation("@Repository");
    return true;
  }
}
