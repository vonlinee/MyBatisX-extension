package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.util.DomUtils;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import org.apache.xmlbeans.xml.stream.XMLName;

import java.util.List;
import java.util.Map;

/**
 * 根据XML标签的内容进行参数提取
 */
public class DefaultMappedStatementParamGetter implements MappedStatementParamGetter {

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
    if (notContainLetterOrNumber(paramKey)) {
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
        if ("test".equals(attributeName) || "when".equals(attributeName)) {
          parseVariableNameFromAttributeValue(attribute.getValue(), paramMap, false);
        }
      }
    } else if (element instanceof XmlText) {
      XmlText text = (XmlText) element;

      ParameterCollector handler = new ParameterCollector();
      List<Parameter> parameters = handler.collect(text.getText());

      for (Parameter parameter : parameters) {
        paramMap.put(parameter.getProperty(), ParamDataType.STRING);
      }
    }
  }

  @Override
  public void getParams(PsiElement element, Map<String, ParamDataType> paramMap) {
    fillParams(element, paramMap, null);
  }
}
