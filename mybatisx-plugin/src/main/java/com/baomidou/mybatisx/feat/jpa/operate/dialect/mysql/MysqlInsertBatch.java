package com.baomidou.mybatisx.feat.jpa.operate.dialect.mysql;

import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomAreaAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.JdbcTypeUtils;
import com.baomidou.mybatisx.feat.jpa.common.appender.operator.SuffixOperator;
import com.baomidou.mybatisx.feat.jpa.common.factory.ResultAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.component.TxReturnDescriptor;
import com.baomidou.mybatisx.feat.jpa.operate.dialect.CustomStatement;
import com.baomidou.mybatisx.feat.jpa.operate.dialect.oracle.InsertCustomSuffixAppender;
import com.baomidou.mybatisx.feat.jpa.operate.manager.StatementBlock;
import com.baomidou.mybatisx.util.MybatisXCollectors;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Mysql insert batch.
 */
public class MysqlInsertBatch implements CustomStatement {

  /**
   * The Statement block.
   */
  StatementBlock statementBlock;
  /**
   * The Operator name.
   */
  String operatorName;


  /**
   * Instantiates a new Mysql insert batch.
   */
  public MysqlInsertBatch() {

  }

  /**
   * Init insert batch.
   *
   * @param areaName     the area name
   * @param mappingField the mapping field
   */
  public void initInsertBatch(String areaName, List<TxField> mappingField) {
    String newAreaName = getNewAreaName(areaName);
    // insertBatch
    ResultAppenderFactory appenderFactory = getResultAppenderFactory(mappingField, newAreaName);
    // insert + Batch
    final SyntaxAppender batchAppender =
      CustomAreaAppender.createCustomAreaAppender(newAreaName,
        ResultAppenderFactory.RESULT,
        AreaSequence.AREA,
        AreaSequence.RESULT,
        appenderFactory);
    appenderFactory.registerAppender(batchAppender);

    StatementBlock statementBlock = new StatementBlock();
    statementBlock.setResultAppenderFactory(appenderFactory);
    statementBlock.setTagName(newAreaName);
    statementBlock.setReturnWrapper(TxReturnDescriptor.createByOrigin(null, "int"));
    this.statementBlock = statementBlock;

    this.operatorName = newAreaName;
  }

  /**
   * Gets result appender factory.
   *
   * @param mappingField the mapping field
   * @param newAreaName  the new area name
   * @return the result appender factory
   */
  protected ResultAppenderFactory getResultAppenderFactory(List<TxField> mappingField, String newAreaName) {

    return new InsertBatchResultAppenderFactory(newAreaName) {
      @Override
      public String getTemplateText(String tableName, PsiClass entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        // 定制参数
        SyntaxAppender suffixOperator = InsertCustomSuffixAppender.createInsertBySuffixOperator(batchName(),
          getSuffixOperator(mappingField),
          AreaSequence.RESULT);
        LinkedList<SyntaxAppenderWrapper> syntaxAppenderWrappers = new LinkedList<>();
        syntaxAppenderWrappers.add(new SyntaxAppenderWrapper(suffixOperator));
        return super.getTemplateText(tableName, entityClass, parameters, syntaxAppenderWrappers, conditionFieldWrapper);
      }
    };
  }

  /**
   * Batch name string.
   *
   * @return the string
   */
  @NotNull
  protected String batchName() {
    return "Batch";
  }

  /**
   * Gets new area name.
   *
   * @param areaName the area name
   * @return the new area name
   */
  @NotNull
  protected String getNewAreaName(String areaName) {
    return areaName + batchName();
  }

  /**
   * Gets suffix operator.
   *
   * @param mappingField the mapping field
   * @return the suffix operator
   */
  @NotNull
  protected SuffixOperator getSuffixOperator(List<TxField> mappingField) {
    return new InsertBatchSuffixOperator(mappingField);
  }

  @Override
  public StatementBlock getStatementBlock() {
    return statementBlock;
  }

  @Override
  public String operatorName() {
    return operatorName;
  }


  private static class InsertBatchResultAppenderFactory extends ResultAppenderFactory {

    /**
     * Instantiates a new Insert batch result appender factory.
     *
     * @param areaPrefix the area prefix
     */
    public InsertBatchResultAppenderFactory(String areaPrefix) {
      super(areaPrefix);
    }

    @Override
    public String getTemplateText(String tableName,
                                  PsiClass entityClass,
                                  LinkedList<TxParameter> parameters,
                                  LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
      StringBuilder mapperXml = new StringBuilder("insert into " + tableName);
      for (SyntaxAppenderWrapper syntaxAppenderWrapper : collector) {
        String templateText = syntaxAppenderWrapper.getAppender()
          .getTemplateText(tableName, entityClass, parameters, collector, conditionFieldWrapper);
        mapperXml.append(templateText);
      }
      return mapperXml.toString();
    }

    @Override
    public List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppenderWrapper> jpaStringList) {
      // 遍历定义的类型
      String defineName = Collection.class.getSimpleName() + "<" + entityClass.getName() + ">";
      // 变量名称
      String variableName = StringUtils.lowerCaseFirstChar(entityClass.getName()) + "Collection";

      List<String> importClass = new ArrayList<>();
      importClass.add("java.util.Collection");
      importClass.add(entityClass.getQualifiedName());
      TxParameter parameter = TxParameter.createByOrigin(variableName, defineName, Collection.class.getName(), true, importClass);
      return Collections.singletonList(parameter);
    }
  }


  /**
   * 批量插入
   */
  private static class InsertBatchSuffixOperator implements SuffixOperator {

    private List<TxField> mappingField;

    /**
     * Instantiates a new Insert batch suffix operator.
     *
     * @param mappingField the mapping field
     */
    public InsertBatchSuffixOperator(List<TxField> mappingField) {
      this.mappingField = mappingField;
    }

    @Override
    public String getTemplateText(String fieldName, LinkedList<TxParameter> parameters, ConditionFieldWrapper conditionFieldWrapper) {
      StringBuilder stringBuilder = new StringBuilder();
      String itemName = "item";
      // 追加列名
      final String columns = mappingField.stream()
        .map(TxField::getColumnName)
        .collect(MybatisXCollectors.joining(",", conditionFieldWrapper.getNewline()));
      stringBuilder.append("(").append(columns).append(")").append("\n");
      // values 连接符
      stringBuilder.append("values").append("\n");
      final TxParameter collection = parameters.poll();
      final String collectionName = collection.getName();
      final String fields = mappingField.stream()
        .map(field -> {
          String fieldValue = JdbcTypeUtils.wrapperField(itemName + "." + field.getFieldName(), field.getFieldType());
          fieldValue = conditionFieldWrapper.wrapDefaultDateIfNecessary(field.getColumnName(), fieldValue);
          return fieldValue;
        })
        .collect(MybatisXCollectors.joining(",", conditionFieldWrapper.getNewline()));

      stringBuilder.append("<foreach collection=\"").append(collectionName).append("\"");
      stringBuilder.append(" item=\"").append(itemName).append("\"");
      stringBuilder.append(" separator=\",\">").append("\n");
      stringBuilder.append("(").append(fields).append(")").append("\n");
      stringBuilder.append("</foreach>");

      return stringBuilder.toString();
    }

  }
}
