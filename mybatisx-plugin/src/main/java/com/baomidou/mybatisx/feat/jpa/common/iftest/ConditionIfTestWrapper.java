package com.baomidou.mybatisx.feat.jpa.common.iftest;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.feat.jpa.common.MapperClassGenerateFactory;
import com.baomidou.mybatisx.feat.jpa.common.appender.JdbcTypeUtils;
import com.baomidou.mybatisx.feat.jpa.component.TxField;
import com.baomidou.mybatisx.feat.jpa.operate.generate.EmptyGenerator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.Generator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.MybatisAnnotationGenerator;
import com.baomidou.mybatisx.feat.jpa.operate.generate.MybatisXmlGenerator;
import com.baomidou.mybatisx.plugin.ui.SmartJpaAdvanceUI;
import com.intellij.openapi.project.Project;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Condition if test wrapper.
 *
 * @author ls9527
 */
public class ConditionIfTestWrapper implements ConditionFieldWrapper {
    public static final int DEFAULT_NEWLINE_VALUE = 3;
    private final Project project;
    private final Set<String> selectedWrapFields;
    private final List<String> resultFields;
    private final Map<String, TxField> txFieldMap;
    private final List<TxField> allFields;
    /**
     * 默认字段的关键字：  oracle: SYSDATE, mysql: NOW()
     */
    private final String defaultDateWord;
    private String allFieldsStr;
    @Setter
    private String resultMap;
    @Setter
    private boolean resultType;
    @Setter
    private String resultTypeClass;
    @Setter
    private SmartJpaAdvanceUI.GeneratorEnum generatorType;
    private Mapper mapper;
    @Setter
    private List<String> defaultDateList;
    private int newLine;

    /**
     * Instantiates a new Condition if test wrapper.
     *
     * @param project
     * @param selectedWrapFields the wrapper fields
     * @param resultFields
     * @param allFields
     * @param defaultDateWord
     */
    public ConditionIfTestWrapper(@NotNull Project project,
                                  Set<String> selectedWrapFields,
                                  List<String> resultFields,
                                  List<TxField> allFields,
                                  String defaultDateWord) {
        this.project = project;
        this.selectedWrapFields = selectedWrapFields;
        this.resultFields = resultFields;
        txFieldMap = allFields.stream().collect(Collectors.toMap(TxField::getFieldName, x -> x, (a, b) -> a));
        this.allFields = allFields;
        this.defaultDateWord = defaultDateWord;
    }

    @Override
    public String wrapConditionText(String fieldName, String templateText) {
        if (selectedWrapFields.contains(fieldName)) {
            templateText = wrapCondition(fieldName, templateText);
        }
        return templateText;
    }

    @NotNull
    private String wrapCondition(String fieldName, String templateText) {
        templateText = "<if test=\"" + getConditionField(fieldName) + "\">" +
                       "\n" + templateText +
                       "\n" + "</if>";
        return templateText;
    }

    private String getConditionField(String fieldName) {
        TxField txField = txFieldMap.get(fieldName);
        String appender = "";
        if (Objects.equals(txField.getFieldType(), "java.lang.String")) {
            appender = " and " + fieldName + " != ''";
        }
        return fieldName + " != null" + appender;
    }

    @Override
    public String wrapWhere(String content) {
        return "<where>\n" + content + "\n</where>";
    }

    @Override
    public String getAllFields() {
        return allFieldsStr;
    }

    /**
     * Sets all fields.
     *
     * @param allFieldsStr the all fields str
     */
    public void setAllFields(String allFieldsStr) {
        this.allFieldsStr = allFieldsStr;
    }

    @Override
    public String getResultMap() {
        return resultType ? null : resultMap;
    }

    @Override
    public String getResultType() {
        return resultTypeClass;
    }

    @Override
    public Boolean isResultType() {
        return resultType;
    }

    @Override
    public Generator getGenerator(MapperClassGenerateFactory mapperClassGenerateFactory) {
        if (this.generatorType == SmartJpaAdvanceUI.GeneratorEnum.MYBATIS_ANNOTATION) {
            return new MybatisAnnotationGenerator(mapperClassGenerateFactory, mapper, project);
        } else if (this.generatorType == SmartJpaAdvanceUI.GeneratorEnum.MYBATIS_XML
                   && mapper != null) {
            return new MybatisXmlGenerator(mapperClassGenerateFactory, mapper, project);
        }
        return new EmptyGenerator();
    }

    @Override
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 对于默认值 create_time,update_time, 在 更新和插入的时候替换为数据库默认值的关键字
     * MYSQL默认时间: NOW()
     * ORACLE默认时间: SYSDATE
     *
     * @param columnName 字段名
     * @param fieldValue
     * @return
     */
    @Override
    public String wrapDefaultDateIfNecessary(String columnName, String fieldValue) {
        if (defaultDateList.contains(columnName)) {
            return defaultDateWord;
        }
        return fieldValue;
    }

    @Override
    public List<String> getDefaultDateList() {
        return defaultDateList;
    }

    @Override
    public List<TxField> getResultTxFields() {
        Set<String> addedFields = new HashSet<>();
        return allFields.stream()
            .filter(field -> resultFields.contains(field.getFieldName()) && addedFields.add(field.getFieldName()))
            .collect(Collectors.toList());
    }

    @Override
    public int getNewline() {
        return newLine;
    }

    @Override
    public String wrapperField(String originName, String name, String canonicalTypeText) {
        TxField txField = txFieldMap.get(originName);
        if (txField != null) {
            String jdbcType = txField.getJdbcType();
            if (jdbcType != null) {
                return "#{" + name +
                       ",jdbcType=" + jdbcType +
                       "}";
            }
        }
        return JdbcTypeUtils.wrapperField(name, canonicalTypeText);
    }

    public void setNewLine(int newLine) {
        // 如果设置错误的值, 给一个合适的默认值
        if (newLine <= 0) {
            newLine = DEFAULT_NEWLINE_VALUE;
        }
        this.newLine = newLine;
    }
}
