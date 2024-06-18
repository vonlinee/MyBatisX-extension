package com.baomidou.mybatisx.service;

import com.baomidou.mybatisx.plugin.actions.Bean2DDLAction;
import com.baomidou.mybatisx.feat.bean.Field;
import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import com.baomidou.mybatisx.util.BaseUtil;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.PsiHelper;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.TranslationUtil;
import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.source.javadoc.PsiDocTokenImpl;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static com.baomidou.mybatisx.util.Constant.BIG_DECIMAL_PACKAGE;
import static com.baomidou.mybatisx.util.Constant.DATE_PACKAGE;
import static com.baomidou.mybatisx.util.Constant.LOCAL_DATE;
import static com.baomidou.mybatisx.util.Constant.LOCAL_DATE_TIME;
import static com.baomidou.mybatisx.util.Constant.LOCAL_TIME;
import static com.baomidou.mybatisx.util.Constant.PRIMARY_KEY_COMMEND;
import static com.baomidou.mybatisx.util.Constant.SERIAL_VERSION_UID;
import static com.baomidou.mybatisx.util.Constant.STRING_PACKAGE;

public final class BeanSqlService {

    public static String getTableName(PsiClass currentClass) {
        OtherSetting.State properties = OtherSetting.getInstance().getProperties();
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

    private static String getTableNameFromClass(PsiClass currentClass) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, Objects.requireNonNull(currentClass.getName()));
    }

    /**
     * 获取需要转换的字段
     */
    public static List<Field> getFieldList(PsiClass currentClass) {
        return getFieldList(currentClass, true);
    }

    /**
     * 获取需要转换的字段
     */
    public static List<Field> getFieldList(PsiClass currentClass, boolean allField) {
        PsiField[] fields = PsiHelper.getPsiFields(currentClass, allField);
        // 利用set去重
        HashSet<Field> fieldSet = new LinkedHashSet<>();
        for (PsiField field : fields) {
            if (isNeedAddConvert(field)) {
                fieldSet.add(getField(field));
            }
        }
        // set command
        List<Field> fieldList = new ArrayList<>(fieldSet);
        if (OtherSetting.getInstance().getProperties().getAutoTranslationRadio()) {
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
     */
    private static String getCommend(Field field, Map<String, String> translationMap) {
        if (!StringUtils.equals(field.getName(), "id")) {
            if (OtherSetting.getInstance().myProperties.getAutoTranslationRadio()) {
                return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), "");
            }
            return translationMap.getOrDefault(field.getTableColumn().replace("_", " "), null);
        }
        return PRIMARY_KEY_COMMEND;
    }

    private static Field getField(PsiField field) {
        boolean primaryKey = false;
        String idAnnotation = OtherSetting.getInstance().getProperties().getIdAnnotation();
        if (StringUtils.isNotBlank(idAnnotation)) {
            PsiAnnotation annotation = field.getAnnotation(idAnnotation);
            if (null != annotation) {
                primaryKey = true;
            }
        }
        return Field.newField(field.getName(), field.getType().getPresentableText(), primaryKey, getComment(field));
    }

    private static String getComment(PsiField field) {
        String commentAnnotation = OtherSetting.getInstance().getProperties().getCommentAnnotation();
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
     */
    private static boolean isNeedAddConvert(PsiField psiField) {
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
        return OtherSetting.getInstance().getProperties().getShowNoInMapFieldRadio();
    }

    private static boolean isAdditional(String canonicalText) {
        return (StringUtils.equals(STRING_PACKAGE, canonicalText)
                || StringUtils.equals(DATE_PACKAGE, canonicalText)
                || StringUtils.equals(BIG_DECIMAL_PACKAGE, canonicalText)
                || StringUtils.equals(LOCAL_DATE, canonicalText)
                || StringUtils.equals(LOCAL_TIME, canonicalText)
                || StringUtils.equals(LOCAL_DATE_TIME, canonicalText));
    }
}
