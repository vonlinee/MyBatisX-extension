package com.baomidou.mybatisx.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

/**
 * The type Mybatis constants.
 *
 * @author yanglin
 */
public final class MyBatisUtils {

  public static final String NAMESPACE = "namespace";

  /**
   * The constant DOT_SEPARATOR.
   */
  public static final String DOT_SEPARATOR = String.valueOf(ReferenceSetBase.DOT_SEPARATOR);

  private MyBatisUtils() {
    throw new UnsupportedOperationException();
  }

  public static boolean isCrudXmlTag(String tagName) {
    return "select".equals(tagName) || "insert".equals(tagName) || "update".equals(tagName) || "delete".equals(tagName);
  }

  @Nullable
  public static String getNamespace(PsiElement element) {
    if (element instanceof XmlElement) {
      return getNamespace((XmlElement) element);
    }
    return null;
  }

  public static String getNamespace(XmlElement xmlElement) {
    XmlFile containingFile = (XmlFile) xmlElement.getContainingFile();
    // Mapper 标签
    XmlDocument document = containingFile.getDocument();
    if (document == null) {
      return null;
    }
    XmlTag rootTag = containingFile.getDocument().getRootTag();
    if (rootTag == null) {
      return null;
    }
    return rootTag.getAttributeValue("namespace");
  }
}
