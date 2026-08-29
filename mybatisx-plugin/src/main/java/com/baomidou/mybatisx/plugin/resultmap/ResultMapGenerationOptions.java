package com.baomidou.mybatisx.plugin.resultmap;

import com.intellij.psi.PsiDirectory;

import java.util.ArrayList;
import java.util.List;

public final class ResultMapGenerationOptions {

  private String resultMapId;
  private String resultType;
  private String className;
  private String packageName;
  private String tableName;
  private boolean generateJavaClass;
  private boolean useLombok = true;
  private boolean generateNoArgsConstructor;
  private boolean jpaEntity;
  private String jpaPackage = "javax.persistence";
  private String generatedValueStrategy = "AUTO";
  private PsiDirectory targetDirectory;
  private final List<SqlColumnModel> columns = new ArrayList<>();
  private final List<String> warnings = new ArrayList<>();

  public String getResultMapId() {
    return resultMapId;
  }

  public void setResultMapId(String resultMapId) {
    this.resultMapId = resultMapId;
  }

  public String getResultType() {
    return resultType;
  }

  public void setResultType(String resultType) {
    this.resultType = resultType;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public boolean isGenerateJavaClass() {
    return generateJavaClass;
  }

  public void setGenerateJavaClass(boolean generateJavaClass) {
    this.generateJavaClass = generateJavaClass;
  }

  public boolean isUseLombok() {
    return useLombok;
  }

  public void setUseLombok(boolean useLombok) {
    this.useLombok = useLombok;
  }

  public boolean isGenerateNoArgsConstructor() {
    return generateNoArgsConstructor;
  }

  public void setGenerateNoArgsConstructor(boolean generateNoArgsConstructor) {
    this.generateNoArgsConstructor = generateNoArgsConstructor;
  }

  public boolean isJpaEntity() {
    return jpaEntity;
  }

  public void setJpaEntity(boolean jpaEntity) {
    this.jpaEntity = jpaEntity;
  }

  public String getJpaPackage() {
    return jpaPackage;
  }

  public void setJpaPackage(String jpaPackage) {
    this.jpaPackage = jpaPackage;
  }

  public String getGeneratedValueStrategy() {
    return generatedValueStrategy;
  }

  public void setGeneratedValueStrategy(String generatedValueStrategy) {
    this.generatedValueStrategy = generatedValueStrategy;
  }

  public PsiDirectory getTargetDirectory() {
    return targetDirectory;
  }

  public void setTargetDirectory(PsiDirectory targetDirectory) {
    this.targetDirectory = targetDirectory;
  }

  public List<SqlColumnModel> getColumns() {
    return columns;
  }

  public List<String> getWarnings() {
    return warnings;
  }
}
