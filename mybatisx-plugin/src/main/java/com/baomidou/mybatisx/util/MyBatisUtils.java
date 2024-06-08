package com.baomidou.mybatisx.util;

import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

/**
 * The type Mybatis constants.
 *
 * @author yanglin
 */
public final class MyBatisUtils {

    /**
     * The constant DOT_SEPARATOR.
     */
    public static final String DOT_SEPARATOR = String.valueOf(ReferenceSetBase.DOT_SEPARATOR);
    /**
     * The constant PRIORITY.
     */
    public static final double PRIORITY = 400.0;

    private MyBatisUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isCrudXmlTag(String text) {
        return "select".equals(text) || "insert".equals(text) || "update".equals(text) || "delete".equals(text);
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
