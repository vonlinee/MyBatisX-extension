package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.util.MapperUtils;
import com.baomidou.mybatisx.util.MyBatisUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.codeInsight.navigation.GotoTargetRendererProvider;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.xml.XmlTagImpl;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public final class GotoMapperXmlSchemaTypeRendererProvider implements GotoTargetRendererProvider {

  @Override
  @Nullable
  public PsiElementListCellRenderer<XmlTag> getRenderer(@NotNull PsiElement element, @NotNull GotoTargetHandler.GotoData gotoData) {
    if (element instanceof XmlTagImpl) {
      if (MapperUtils.isElementWithinMybatisFile(element)) {
        return new MapperXmlTagListCellRender();
      }
    }
    return null;
  }

  public static class MapperXmlTagListCellRender extends PsiElementListCellRenderer<XmlTag> {

    @Override
    public String getElementText(XmlTag element) {
      XmlAttribute attr = element.getAttribute("id", XmlUtil.XML_SCHEMA_URI);
      attr = attr == null ? element.getAttribute("id") : attr;
      return (attr == null || attr.getValue() == null ? element.getName() : attr.getValue());
    }

    @Override
    protected String getContainerText(XmlTag element, String name) {
      String databaseId = MyBatisUtils.getDatabaseId(element);
      if (StringUtils.isBlank(databaseId)) {
        return PsiUtils.getProjectRelativePath(element);
      }
      return "[" + databaseId + "]" + PsiUtils.getProjectRelativePath(element);
    }
  }
}
