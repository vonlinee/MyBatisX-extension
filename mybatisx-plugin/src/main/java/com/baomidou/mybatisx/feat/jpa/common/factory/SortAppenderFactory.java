package com.baomidou.mybatisx.feat.jpa.common.factory;


import com.baomidou.mybatisx.feat.jpa.common.BaseAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppenderFactory;
import com.baomidou.mybatisx.feat.jpa.common.iftest.ConditionFieldWrapper;
import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;
import com.baomidou.mybatisx.feat.jpa.common.appender.AreaSequence;
import com.baomidou.mybatisx.feat.jpa.common.appender.CompositeAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomAreaAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomFieldAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomJoinAppender;
import com.baomidou.mybatisx.feat.jpa.common.appender.CustomSuffixAppender;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.component.TxParameter;
import com.baomidou.mybatisx.feat.jpa.SyntaxAppenderWrapper;
import com.intellij.psi.PsiClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 排序区
 */
public class SortAppenderFactory extends BaseAppenderFactory {
    private final List<TxField> mappingField;

    /**
     * Instantiates a new Sort appender factory.
     *
     * @param mappingField the mapping field
     */
    public SortAppenderFactory(final List<TxField> mappingField) {
        this.mappingField = mappingField;
    }

    @Override
    public List<SyntaxAppender> getSyntaxAppenderList() {
        final List<SyntaxAppender> syntaxAppenderArrayList = new ArrayList<>();
        // order by field : desc
        syntaxAppenderArrayList.add(CustomSuffixAppender.createByFixed("Desc", "desc", AreaSequence.SORT, mappingField));
        for (final TxField field : this.mappingField) {
            // order by: field
            final SyntaxAppender appender = new CompositeAppender(
                    new SortCustomAreaAppender(this.getTipText(), getTipText(),
                            AreaSequence.AREA,
                            AreaSequence.SORT,
                            this),
                    new SortCustomFieldAppender(field, AreaSequence.SORT));
            syntaxAppenderArrayList.add(appender);
            // order by: and field
            final CompositeAppender andAppender = new CompositeAppender(new CustomJoinAppender("And", ",", AreaSequence.SORT),
                    new SortCustomFieldAppender(field, AreaSequence.SORT));
            syntaxAppenderArrayList.add(andAppender);
        }
        return syntaxAppenderArrayList;
    }

    @Override
    public String getTipText() {
        return "OrderBy";
    }

    @Override
    public List<TxParameter> getMxParameter(PsiClass entityClass, LinkedList<SyntaxAppenderWrapper> jpaStringList) {
        return Collections.emptyList();
    }

    @Override
    public String getTemplateText(String tableName, PsiClass entityClass, LinkedList<TxParameter> parameters, LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
        StringBuilder stringBuilder = new StringBuilder();
        for (SyntaxAppenderWrapper syntaxAppender : collector) {
            String templateText = syntaxAppender.getAppender()
                    .getTemplateText(tableName, entityClass, parameters, syntaxAppender.getCollector(), conditionFieldWrapper);
            stringBuilder.append(templateText).append(" ");
        }
        return "order by " + stringBuilder.toString();
    }

    @Override
    public void appendDefault(SyntaxAppender syntaxAppender, LinkedList<SyntaxAppender> current) {
        if (syntaxAppender.getType() == AppendTypeEnum.FIELD) {
            current.addLast(CustomSuffixAppender.createByFixed("Asc", "asc", AreaSequence.SORT, mappingField));
        }
    }

    @Override
    protected AreaSequence getAreaSequence() {
        return AreaSequence.SORT;
    }

    private class SortCustomAreaAppender extends CustomAreaAppender {

        public SortCustomAreaAppender(String area, String areaType, AreaSequence areaSequence, AreaSequence childAreaSequence, SyntaxAppenderFactory syntaxAppenderFactory) {
            super(area, areaType, areaSequence, childAreaSequence, syntaxAppenderFactory);
        }

        /**
         * OrderBy 标签的字段一定不会生成参数
         *
         * @param syntaxAppenderWrappers the jpa string list
         * @param entityClass            the entity class
         * @return
         */
        @Override
        public List<TxParameter> getMxParameter(LinkedList<SyntaxAppenderWrapper> syntaxAppenderWrappers, PsiClass entityClass) {
            return Collections.emptyList();
        }
    }

    private class SortCustomFieldAppender extends CustomFieldAppender {

        /**
         * Instantiates a new Sort custom field appender.
         *
         * @param field the field
         * @param sort  the sort
         */
        public SortCustomFieldAppender(TxField field, AreaSequence sort) {
            super(field, sort);
        }

        @Override
        public String getTemplateText(String tableName,
                                      PsiClass entityClass,
                                      LinkedList<TxParameter> parameters,
                                      LinkedList<SyntaxAppenderWrapper> collector, ConditionFieldWrapper conditionFieldWrapper) {
            return getFieldName();
        }
    }
}
