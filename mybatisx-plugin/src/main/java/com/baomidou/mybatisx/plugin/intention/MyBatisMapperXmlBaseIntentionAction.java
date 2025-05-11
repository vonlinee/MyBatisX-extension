package com.baomidou.mybatisx.plugin.intention;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

/**
 * MyBatis Mapper Xml 中的提示
 */
public abstract class MyBatisMapperXmlBaseIntentionAction extends PsiElementBaseIntentionAction {

  public boolean isMyBatisMapperXmlFile(PsiFile psiFile) {
    if (psiFile instanceof XmlFile) {
      XmlFile xmlFile = (XmlFile) psiFile;
      XmlDocument document = xmlFile.getDocument();
      if (document == null) {
        return false;
      }
      XmlTag rootTag = document.getRootTag();
      if (rootTag == null) {
        return false;
      }
      return "mapper".equals(rootTag.getName());
    }
    return false;
  }

  @Override
  public @NotNull
  @IntentionFamilyName String getFamilyName() {
    return getText();
  }
}
