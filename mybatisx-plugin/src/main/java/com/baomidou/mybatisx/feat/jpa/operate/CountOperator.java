package com.baomidou.mybatisx.feat.jpa.operate;

import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CompositeAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomAreaAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.common.factory.ConditionAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.factory.ResultAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.component.TxReturnDescriptor;
import com.baomidou.mybatisx.feat.jpa.operate.appender.SelectCustomAreaAppender;
import com.baomidou.mybatisx.feat.jpa.operate.generate.Generator;
import com.baomidou.mybatisx.feat.jpa.operate.manager.StatementBlock;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author ls9527
 */
public class CountOperator extends SelectOperator {
    public CountOperator(List<TxField> mappingField, PsiClass entityClass) {
        super(mappingField, entityClass);
    }

    @Override
    protected Set<String> getPatterns() {
        return Collections.singleton("count");
    }

    @Override
    protected ResultAppenderFactory initCustomFieldResultAppender(
        final List<TxField> mappingField,
        final String areaName,
        ConditionAppenderFactory conditionAppenderFactory) {
        ResultAppenderFactory selectFactory = new ResultAppenderFactory(areaName) {
            @Override
            public String getTemplateText(String tableName, PsiClass entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
                return "select count(*) \n" +
                       " from " + tableName;
            }
        };

        // 区域条件 : count
        selectFactory.registerAppender(new SelectCustomAreaAppender(areaName, ResultAppenderFactory.RESULT, selectFactory));
        // 区域条件 : count + By
        for (TxField txField : mappingField) {
            CompositeAppender areaByAppender = new CompositeAppender(
                new SelectCustomAreaAppender(areaName, ResultAppenderFactory.RESULT, selectFactory),
                CustomAreaAppender.createCustomAreaAppender("By",
                    "By",
                    AreaSequence.AREA,
                    AreaSequence.CONDITION,
                    conditionAppenderFactory),
                new CustomFieldAppender(txField, AreaSequence.CONDITION)
            );
            selectFactory.registerAppender(areaByAppender);
        }


        return selectFactory;
    }

    @Override
    public String getTagName() {
        return "count";
    }

    @Override
    public void init(List<TxField> mappingField, PsiClass entityClass, Set<String> patterns) {
        initCustomFieldBlock(getTagName(), mappingField, entityClass);
    }

    private void initCustomFieldBlock(String areaName, List<TxField> mappingField, PsiClass entityClass) {
        StatementBlock statementBlock = new StatementBlock();
        ConditionAppenderFactory conditionAppenderFactory = new ConditionAppenderFactory(areaName, mappingField);
        statementBlock.setConditionAppenderFactory(conditionAppenderFactory);

        // 结果集区域

        ResultAppenderFactory resultAppenderFactory =
            this.initCustomFieldResultAppender(mappingField, areaName, conditionAppenderFactory);

        statementBlock.setResultAppenderFactory(resultAppenderFactory);

        statementBlock.setTagName(areaName);
        statementBlock.setReturnWrapper(TxReturnDescriptor.createByOrigin(null, "int"));
        this.registerStatementBlock(statementBlock);

        this.addOperatorName(areaName);
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

        mybatisXmlGenerator.generateSelect(id,
            mapperXml,
            true,
            null,
            "int", resultFields, entityClass);
    }
}
