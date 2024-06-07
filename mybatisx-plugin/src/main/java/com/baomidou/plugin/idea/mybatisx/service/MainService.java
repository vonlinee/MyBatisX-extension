package com.baomidou.plugin.idea.mybatisx.service;

import com.baomidou.plugin.idea.mybatisx.actions.Bean2DDLAction;
import com.baomidou.plugin.idea.mybatisx.model.Field;
import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;
import com.baomidou.plugin.idea.mybatisx.util.BaseUtil;
import com.baomidou.plugin.idea.mybatisx.util.CollectionUtils;
import com.baomidou.plugin.idea.mybatisx.util.PsiHelper;
import com.baomidou.plugin.idea.mybatisx.util.TranslationUtil;
import com.google.common.base.CaseFormat;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.baomidou.plugin.idea.mybatisx.util.Constant.*;

public class MainService {

    public String getTableName(PsiClass currentClass) {
        JavaBean2DDLSetting.MySettingProperties properties = JavaBean2DDLSetting.getInstance().myProperties;
        String tableAnnotation = properties.getTableAnnotation();
        if (StringUtils.isBlank(tableAnnotation)) {
            // table注解为空直接返回类名作为表名
            return getTableNameFromClass(currentClass);
        }
        PsiAnnotation annotation = currentClass.getAnnotation(tableAnnotation);
        if (null == annotation || null == annotation.findAttributeValue(properties.getTableAnnotationProperty())) {
            return getTableNameFromClass(currentClass);
        }
        PsiAnnotationMemberValue value = annotation.findAttributeValue(properties.getTableAnnotationProperty());
        if (null == value || StringUtils.isBlank(value.getText())) {
            return getTableNameFromClass(currentClass);
        }
        // return PsiLiteralUtil.getStringLiteralContent(((PsiLiteralExpressionImpl) name));
        // 低版本兼容
        // return ((PsiLiteralExpressionImpl) name).getInnerText();
        String tableName = BaseUtil.getStringLiteralContent(((PsiLiteralExpressionImpl) value));
        return StringUtils.isNotBlank(tableName) ? tableName : getTableNameFromClass(currentClass);
    }

    private String getTableNameFromClass(PsiClass currentClass) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, Objects.requireNonNull(currentClass.getName()));
    }

    /**
     * 获取需要转换的字段
     *
     * @param currentClass
     * @return
     */
    public List<Field> getFieldList(PsiClass currentClass) {
        return getFieldList(currentClass, true);
    }

    /**
     * 获取需要转换的字段
     *
     * @param currentClass
     * @return
     */
    public List<Field> getFieldList(PsiClass currentClass, boolean allField) {
        PsiField[] fields = getPsiFields(currentClass, allField);
        // 利用set去重
        HashSet<Field> fieldSet = new LinkedHashSet<>();
        for (PsiField field : fields) {
            if (isNeedAddConvert(field)) {
                fieldSet.add(getField(field));
            }
        }

        // set command
        List<Field> fieldList = new ArrayList<>(fieldSet);
        if (JavaBean2DDLSetting.getInstance().myProperties.getAutoTranslationRadio()) {
            getTranslationMap(fieldList, getTableName(currentClass));
        }
        for (Field field : fieldList) {
            if (StringUtils.isBlank(field.getComment())) {
                field.setComment(getCommend(field, Bean2DDLAction.translationMap));
            }
        }
        return fieldList;
    }

    /**
     * 翻译字段得到注释
     *
     * @param fieldList
     * @param tableName
     * @return
     */
    private static Map<String, String> getTranslationMap(List<Field> fieldList, String tableName) {
        if (null != Bean2DDLAction.translationMap && !Bean2DDLAction.translationMap.isEmpty()) {
            return Bean2DDLAction.translationMap;
        }
        Map<String, String> translationMap = TranslationUtil.enToZh(fieldList, tableName);
        Bean2DDLAction.translationMap = new ConcurrentHashMap<>(translationMap);
        return new ConcurrentHashMap<>(translationMap);
    }

    /**
     * 获取注释
     *
     * @param field
     * @param translationMap
     * @return
     */
    private static String getCommend(Field field, Map<String, String> translationMap) {
        if (!StringUtils.equals(field.getName(), "id")) {
            if (JavaBean2DDLSetting.getInstance().myProperties.getAutoTranslationRadio()) {
                return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), "");
            }
            return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), null);
        }
        return PRIMARY_KEY_COMMEND;
    }


    private PsiField[] getPsiFields(PsiClass currentClass, boolean allField) {
        if (allField) {
            return BaseUtil.getAllFields(currentClass);
        }
        return BaseUtil.getFields(currentClass);
    }

    private Field getField(PsiField field) {
        boolean primaryKey = false;
        String idAnnotation = JavaBean2DDLSetting.getInstance().myProperties.getIdAnnotation();
        if (StringUtils.isNotBlank(idAnnotation)) {
            PsiAnnotation annotation = field.getAnnotation(idAnnotation);
            if (null != annotation) {
                primaryKey = true;
            }
        }
        return Field.newField(field.getName(), field.getType().getPresentableText(), primaryKey, getComment(field));
    }

    private String getComment(PsiField field) {
        String commentAnnotation = JavaBean2DDLSetting.getInstance().myProperties.getCommentAnnotation();
        if (null != field.getDocComment() && StringUtils.isNotBlank(commentAnnotation)) {
            // 字段上携带了对应的标识备注注解
            PsiDocTag comment = field.getDocComment().findTagByName(commentAnnotation);
            if (null != comment && null != comment.getValueElement()) {
                return comment.getValueElement().getText();
            }
            // 字段上没有携带注解，则从Doc注释上获取
            PsiDocComment docComment = field.getDocComment();
            if (null != docComment) {
                PsiElement[] elements = docComment.getDescriptionElements();
                return parseAllDoc(elements);
            }
        }
        return null;
    }

    /**
     * 解析出文档所有有效文本
     */
    public static String parseAllDoc(PsiElement[] elements) {
        if (elements == null || elements.length == 0) {
            return null;
        }
        List<String> docList = new ArrayList<>();
        for (PsiElement element : elements) {
            if (element instanceof PsiWhiteSpaceImpl) {
                continue;
            }
            if (element instanceof PsiDocTokenImpl) {
                String text = element.getText();
                if (StringUtils.isNotBlank(text)) {
                    docList.add(text.trim());
                }
            }
        }
        return CollectionUtils.isEmpty(docList) ? StringUtils.EMPTY : String.join(" ", docList);
    }

    /**
     * 字段类型是否需要加入转换
     *
     * @param psiField
     * @return
     */
    private boolean isNeedAddConvert(PsiField psiField) {
        if (null == psiField) {
            return false;
        }
        // 忽略 serialVersionUID
        if (SERIAL_VERSION_UID.equals(psiField.getName())) {
            return false;
        }
        if (PsiHelper.isPrimitiveType(psiField)) {
            return true;
        }
        // 额外可支持的类型
        String canonicalText = psiField.getType().getCanonicalText();
        if (isAdditional(canonicalText)) {
            return true;
        }
        // 是否开启显示所有非常用字段
        return JavaBean2DDLSetting.getInstance().myProperties.getShowNoInMapFieldRadio();
    }

    private boolean isAdditional(String canonicalText) {
        return (StringUtils.equals(STRING_PACKAGE, canonicalText)
                || StringUtils.equals(DATE_PACKAGE, canonicalText)
                || StringUtils.equals(BIG_DECIMAL_PACKAGE, canonicalText)
                || StringUtils.equals(LOCAL_DATE, canonicalText)
                || StringUtils.equals(LOCAL_TIME, canonicalText)
                || StringUtils.equals(LOCAL_DATE_TIME, canonicalText));
    }

}
