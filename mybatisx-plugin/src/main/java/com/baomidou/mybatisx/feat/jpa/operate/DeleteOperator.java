package com.baomidou.mybatisx.feat.jpa.operate;


import com.baomidou.mybatisx.plugin.setting.config.AbstractStatementGenerator;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CompositeAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomAreaAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomJoinAppender;
import com.baomidou.mybatisx.feat.jpa.common.factory.ConditionAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.factory.ResultAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.component.TxReturnDescriptor;
import com.baomidou.mybatisx.feat.jpa.operate.generate.Generator;
import com.baomidou.mybatisx.feat.jpa.operate.manager.StatementBlock;
import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.LinkedList;
import java.util.List;

/**
 * The type Delete operator.
 */
public class DeleteOperator extends BaseOperatorManager {


    /**
     * Instantiates a new Delete operator.
     *
     * @param mappingField the mapping field
     */
    public DeleteOperator(final List<TxField> mappingField) {
        super.setOperatorNameList(AbstractStatementGenerator.DELETE_GENERATOR.getPatterns());
        this.init(mappingField);
    }


    /**
     * Init.
     *
     * @param mappingField the mapping field
     */
    public void init(final List<TxField> mappingField) {
        for (String areaName : getOperatorNameList()) {
            // 没有结果集字段
            final ResultAppenderFactory resultAppenderFactory = new DeleteResultAppenderFactory(areaName);
            ConditionAppenderFactory conditionAppenderFactory = new ConditionAppenderFactory(areaName, mappingField);
            for (TxField field : mappingField) {
                // 区域条件 : delete + By + field
                CompositeAppender areaByAppender = new CompositeAppender(
                        CustomAreaAppender.createCustomAreaAppender(areaName, ResultAppenderFactory.RESULT, AreaSequence.AREA, AreaSequence.RESULT, resultAppenderFactory),
                        CustomAreaAppender.createCustomAreaAppender("By", "By", AreaSequence.AREA, AreaSequence.CONDITION, conditionAppenderFactory),
                        new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(areaByAppender);

                // 区域条件 : delete  By : and + field
                CompositeAppender andAppender = new CompositeAppender(
                        new CustomJoinAppender("And", " AND", AreaSequence.CONDITION),
                        new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(andAppender);

                // 区域条件 : delete  By : or + field
                CompositeAppender orAppender = new CompositeAppender(
                        new CustomJoinAppender("Or", " OR", AreaSequence.CONDITION),
                        new CustomFieldAppender(field, AreaSequence.CONDITION)
                );
                resultAppenderFactory.registerAppender(orAppender);
            }

            StatementBlock statementBlock = new StatementBlock();
            statementBlock.setTagName(areaName);
            statementBlock.setResultAppenderFactory(resultAppenderFactory);
            statementBlock.setConditionAppenderFactory(conditionAppenderFactory);
            statementBlock.setReturnWrapper(TxReturnDescriptor.createByOrigin(null, "int"));
            this.registerStatementBlock(statementBlock);
        }

    }

    @Override
    public String getTagName() {
        return "delete";
    }

    @Override
    public void generateMapperXml(String id,
                                  LinkedList<SyntaxAppender> jpaList,
                                  PsiClass entityClass,
                                  PsiMethod psiMethod,
                                  String tableName,
                                  Generator mybatisXmlGenerator,
                                  ConditionFieldWrapper conditionFieldWrapper,
                                  List<TxField> resultFields) {
        String mapperXml = super.generateXml(jpaList, entityClass, psiMethod, tableName, conditionFieldWrapper);
        mybatisXmlGenerator.generateDelete(id, mapperXml);
    }

    private static class DeleteResultAppenderFactory extends ResultAppenderFactory {

        /**
         * Instantiates a new Delete result appender factory.
         *
         * @param pattern the pattern
         */
        public DeleteResultAppenderFactory(String pattern) {
            super(pattern);
        }

        @Override
        public String getTemplateText(String tableName, PsiClass entityClass,
                                      LinkedList<TxParameter> parameters,
                                      LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {


            return "delete from " + tableName;
        }
    }
}
