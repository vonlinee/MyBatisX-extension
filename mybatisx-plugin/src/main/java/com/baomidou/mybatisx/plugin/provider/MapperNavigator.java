package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.util.MyBatisUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;

import javax.swing.*;

public interface MapperNavigator {

  /**
   * 显示的图标
   *
   * @return 图标
   */
  Icon getIcon();

  /**
   * 显示的文本
   *
   * @return 显示的文本
   */
  String getDisplayText();

  void navigate(Project project, MapperStatementItem item);

  String getNavigationGroupName();

  /**
   * 搜索Mapper Statement标签对应的PSI元素
   *
   * @param lineElement 每行标记的标签
   * @return Xml标签元素
   */
  default XmlTag findMapperStatementTag(PsiElement lineElement) {
    if (lineElement instanceof XmlToken) {
      return findMapperStatementTag(lineElement.getParent());
    } else if (lineElement instanceof XmlTag) {
      XmlTag tag = (XmlTag) lineElement;
      if (MyBatisUtils.isCrudXmlTag(tag.getName())) {
        return tag;
      }
    }
    return null;
  }
}
