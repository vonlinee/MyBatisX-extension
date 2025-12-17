package com.baomidou.mybatisx.plugin.intention;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.XmlRecursiveElementVisitor;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlComment;
import com.intellij.psi.xml.XmlDecl;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlProlog;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import com.intellij.psi.xml.XmlToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @see com.intellij.psi.XmlRecursiveElementVisitor
 */
public class MappedStatementTextBuilder extends XmlRecursiveElementVisitor {

  private final XmlTag root;

  private final StringBuilder text = new StringBuilder();

  private final Map<String, XmlTag> sqlTagMap;

  public MappedStatementTextBuilder(XmlTag root) {
    this(root, null);
  }

  private MappedStatementTextBuilder(XmlTag root, Map<String, XmlTag> sqlTagMap) {
    this.root = root;
    if (sqlTagMap == null) {
      Map<String, XmlTag> sqlTags = new HashMap<>();
      XmlFile containingFile = (XmlFile) root.getContainingFile();
      XmlTag rootTag = containingFile.getRootTag();
      if (rootTag != null) {
        for (PsiElement child : rootTag.getChildren()) {
          if (child instanceof XmlTag) {
            XmlTag childTag = (XmlTag) child;
            String name = childTag.getName();
            if ("sql".equalsIgnoreCase(name)) {
              String id = childTag.getAttributeValue("id");
              if (id != null) {
                sqlTags.put(id, childTag);
              }
            }
          }
        }
      }
      this.sqlTagMap = sqlTags;
    } else {
      this.sqlTagMap = sqlTagMap;
    }
  }

  @Override
  public void visitXmlElement(XmlElement element) {
    super.visitElement(element);
  }

  @Override
  public void visitXmlFile(XmlFile file) {
    super.visitXmlFile(file);
  }

  @Override
  public void visitXmlAttribute(XmlAttribute attribute) {
    text.append(attribute.getText());
  }

  @Override
  public void visitXmlComment(XmlComment comment) {
    text.append(comment.getText());
  }

  @Override
  public void visitXmlDecl(XmlDecl decl) {
    text.append(decl.getText());
  }

  @Override
  public void visitXmlDocument(XmlDocument document) {
    super.visitXmlDocument(document);
  }

  @Override
  public void visitXmlProlog(XmlProlog prolog) {
    text.append(prolog.getText());
  }

  @Override
  public void visitXmlText(XmlText xmlText) {
    text.append(xmlText.getText());
  }

  @Override
  public void visitXmlTag(XmlTag tag) {
    if ("include".equals(tag.getName())) {
      String refId = tag.getAttributeValue("refid");
      if (refId != null) {
        XmlTag xmlTag = sqlTagMap.get(refId);
        if (xmlTag != null) {

          MappedStatementTextBuilder builder = new MappedStatementTextBuilder(xmlTag, this.sqlTagMap);
          xmlTag.accept(builder);
          String textInChild = builder.getText();
          // remove <sql ....>
          int i = textInChild.indexOf(">");
          if (i > 0) {
            textInChild = textInChild.substring(i + 1);
          }
          textInChild = textInChild.replace("</sql>", "");

          // 去掉include的内容的空格
          textInChild = textInChild.trim();

          text.append(textInChild);
        }
      }
    } else {
      tag.acceptChildren(this);
    }
  }

  @Override
  public void visitWhiteSpace(@NotNull PsiWhiteSpace space) {
    text.append(space.getText());
  }

  @Override
  public void visitXmlToken(XmlToken token) {
    text.append(token.getText());
  }

  @Override
  public void visitXmlAttributeValue(XmlAttributeValue value) {
    text.append(value.getText());
  }

  public String getText() {
    return text.toString();
  }

  public static String build(XmlTag tag) {
    MappedStatementTextBuilder builder = new MappedStatementTextBuilder(tag);
    tag.accept(builder);
    return builder.getText();
  }
}
