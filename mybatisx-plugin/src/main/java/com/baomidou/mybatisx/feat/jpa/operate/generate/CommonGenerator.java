package com.baomidou.mybatisx.feat.jpa.operate.generate;

import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.component.TxParameterDescriptor;
import com.baomidou.mybatisx.feat.jpa.component.TypeDescriptor;
import com.baomidou.mybatisx.feat.jpa.db.DasTableAdaptor;
import com.baomidou.mybatisx.feat.jpa.db.DbmsAdaptor;
import com.baomidou.mybatisx.feat.jpa.operate.manager.AreaOperateManager;
import com.baomidou.mybatisx.feat.jpa.operate.manager.AreaOperateManagerFactory;
import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;
import com.intellij.openapi.application.WriteAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 常用的生成器
 *
 * @author ls9527
 */
public class CommonGenerator implements PlatformGenerator {
  /**
   * The Appender manager.
   */
  final AreaOperateManager appenderManager;
  private final String defaultDateWord;
  private final @NotNull
  LinkedList<SyntaxAppender> jpaList;
  private final List<TxField> mappingField;
  private final String tableName;
  private final PsiClass entityClass;
  private final String text;
  private final Set<String> notNeedsResult = new HashSet<>() {
    {
      add("update");
      add("insert");
      add("delete");
    }
  };

  private CommonGenerator(PsiClass entityClass,
                          String text,
                          DbmsAdaptor dbms,
                          DasTableAdaptor dasTable,
                          String tableName,
                          List<TxField> fields) {
    this.entityClass = entityClass;
    this.text = text;
    mappingField = fields;
    defaultDateWord = dbms.getDefaultDateWord();
    this.tableName = tableName;

    appenderManager = AreaOperateManagerFactory.getAreaOperateManagerByDbms(dbms, mappingField, entityClass, dasTable, this.tableName);
    jpaList = appenderManager.splitAppenderByText(text);
  }

  /**
   * Create editor auto completion common generator.
   *
   * @param entityClass the entity class
   * @param text        the text
   * @param dbms        the dbms
   * @param dasTable    the das table
   * @param tableName   the table name
   * @param fields      the fields
   * @return common generator
   */
  public static CommonGenerator createEditorAutoCompletion(PsiClass entityClass, String text,
                                                           @NotNull DbmsAdaptor dbms,
                                                           DasTableAdaptor dasTable,
                                                           String tableName,
                                                           List<TxField> fields) {
    return new CommonGenerator(entityClass, text, dbms, dasTable, tableName, fields);
  }

  @Override
  public String getDefaultDateWord() {
    return defaultDateWord;
  }

  @Override
  public TypeDescriptor getParameter() {
    List<TxParameter> parameters = appenderManager.getParameters(entityClass, new LinkedList<>(jpaList));
    return new TxParameterDescriptor(parameters, mappingField);
  }

  @Override
  public TypeDescriptor getReturn() {
    LinkedList<SyntaxAppender> linkedList = new LinkedList<>(jpaList);
    return appenderManager.getReturnWrapper(text, entityClass, linkedList);
  }

  @Override
  public void generateMapperXml(PsiMethod psiMethod,
                                ConditionFieldWrapper conditionFieldWrapper,
                                List<TxField> resultFields,
                                Generator generator) {
    WriteAction.run(() -> {
      // 生成完整版的内容
      appenderManager.generateMapperXml(
        text,
        new LinkedList<>(jpaList),
        entityClass,
        psiMethod,
        tableName,
        generator,
        conditionFieldWrapper,
        resultFields);
    });
  }

  @Override
  public List<String> getConditionFields() {
    return jpaList.stream()
      .filter(syntaxAppender -> syntaxAppender.getAreaSequence() == AreaSequence.CONDITION
                                && syntaxAppender.getType() == AppendTypeEnum.FIELD &&
                                syntaxAppender instanceof CustomFieldAppender)
      .flatMap(x -> Arrays.stream(((CustomFieldAppender) x).getFieldName().split(",")))
      .collect(Collectors.toList());
  }

  @Override
  public List<TxField> getAllFields() {
    return mappingField;
  }

  @Override
  public PsiClass getEntityClass() {
    return entityClass;
  }

  @Override
  public List<String> getResultFields() {
    SyntaxAppender peek = jpaList.peek();
    if (peek == null || notNeedsResult.contains(peek.getText())) {
      return Collections.emptyList();
    }
    return jpaList.stream()
      .filter(syntaxAppender -> syntaxAppender.getAreaSequence() == AreaSequence.RESULT
                                && syntaxAppender.getType() == AppendTypeEnum.FIELD &&
                                syntaxAppender instanceof CustomFieldAppender)
      .flatMap(x -> Arrays.stream(x.getText().split(",")))
      .collect(Collectors.toList());
  }
}
