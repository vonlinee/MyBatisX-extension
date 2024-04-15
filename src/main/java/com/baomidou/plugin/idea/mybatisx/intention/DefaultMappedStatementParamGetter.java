package com.baomidou.plugin.idea.mybatisx.intention;

import com.baomidou.plugin.idea.mybatisx.model.ParamDataType;
import com.baomidou.plugin.idea.mybatisx.util.DomUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLName;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据XML标签的内容进行参数提取
 */
public class DefaultMappedStatementParamGetter implements MappedStatementParamGetter {
    /**
     * 正则表达式匹配 ${...} 或 #{...} 中的内容
     */
    Pattern pattern = Pattern.compile("\\$\\{(.*?)}|#\\{(.*?)}");

    public void fillParams(PsiElement element, Map<String, ParamDataType> paramMap, XmlElement parent) {
        if (element instanceof XMLName) {
            return;
        }
        if (element instanceof XmlTag) {
            // 标签属性中的变量引用
            XmlTag tag = (XmlTag) element;
            if ("foreach".equals(tag.getName())) {
                String collection = DomUtils.getAttributeValue(tag, "collection");
                parseVariableNameFromAttributeValue(collection, paramMap, true);
                for (PsiElement child : tag.getChildren()) {
                    if (child instanceof XmlAttribute
                        || child instanceof XMLName) {
                        continue;
                    }
                    if (child instanceof XmlToken) {
                        continue;
                    }
                    fillParams(child, paramMap, tag);
                }
            } else {
                for (PsiElement child : tag.getChildren()) {
                    fillParams(child, paramMap, tag);
                }
            }
        } else if (element instanceof XmlToken) {
            XmlToken token = (XmlToken) element;
            String text = token.getText();
        } else if (element instanceof XmlAttribute) {
            XmlAttribute attribute = (XmlAttribute) element;
            if (attribute.getValue() != null) {
                String attributeName = attribute.getName();
                // 条件表达式
                if ("test".equals(attributeName) || "when".equals(attributeName)) {
                    parseVariableNameFromAttributeValue(attribute.getValue(), paramMap, false);
                }
            }
        } else if (element instanceof XmlText) {
            XmlText text = (XmlText) element;
            parseVariableReference(text.getText(), paramMap, parent);
        }
    }

    // TODO 类型推断
    public void parseVariableReference(String text, Map<String, ParamDataType> paramMap, XmlElement parent) {

        String itemName = null;
        if (parent instanceof XmlTag) {
            XmlTag tag = (XmlTag) parent;
            if ("foreach".equals(tag.getName())) {
                // item 属性不作为变量名
                itemName = DomUtils.getAttributeValue(tag, "item");
            }
        }

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            // matcher.group(1) 会返回第一个括号内的内容，即变量名
            for (int i = 0; i < matcher.groupCount(); i++) {
                String variable = matcher.group(i);
                if (variable == null) {
                    continue;
                }
                // 去掉一个字符串前后的除数字和字母外的字符
                variable = variable.replaceAll("^[^a-zA-Z0-9]+|[^a-zA-Z0-9]+$", "");

                if (itemName != null && Objects.equals(variable, itemName)) {
                    continue;
                }

                paramMap.put(variable, ParamDataType.STRING);
            }
        }
    }

    /**
     * 从属性中解析变量，普通字符串，不使用
     *
     * @param attributeValue 属性值， 例如：user.name != null and age > 10
     */
    public static void parseVariableNameFromAttributeValue(String attributeValue, Map<String, ParamDataType> variableNameMap, boolean array) {
        String[] tokens = splitWithBlankSpace(attributeValue);
        for (int i = 0; i < tokens.length; i++) {
            if (i + 1 < tokens.length) {
                if (!containLetterOrNumber(tokens[i + 1])) {
                    addParam(variableNameMap, tokens[i], array);
                }
            } else {
                if (tokens[i].indexOf(".") > 0) {
                    addParam(variableNameMap, tokens[i], array);
                } else if (!notContainLetterOrNumber(tokens[i])) {
                    if (i - 1 >= 0) {
                        if (containLetterOrNumber(tokens[i - 1])) {
                            addParam(variableNameMap, tokens[i], array);
                        }
                    } else {
                        // 单个变量，一般是 boolean 表达式
                        if ("true".equals(tokens[i]) || "false".equals(tokens[i])) {

                        } else {
                            if (containLetterOrNumber(tokens[i])) {
                                addParam(variableNameMap, tokens[i], array);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void addParam(Map<String, ParamDataType> variableNameMap, String paramKey, boolean array) {
        if (paramKey == null || paramKey.isEmpty()) {
            return;
        }
        if (paramKey.contains("(") || paramKey.contains(")")) {
            return;
        }
        variableNameMap.put(paramKey, array ? ParamDataType.NUMBER_ARRAY : ParamDataType.STRING);
    }

    public static boolean notContainLetterOrNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) || Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containLetterOrNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)) || Character.isLetter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    // split()方法默认会匹配所有的空格，包括单个空格、多个连续的空格以及换行符后的空格
    public static String[] splitWithBlankSpace(String str) {
        return str.split(" ");
    }

    @Override
    public void getParams(PsiElement element, Map<String, ParamDataType> paramMap) {
        fillParams(element, paramMap, null);
    }
}
