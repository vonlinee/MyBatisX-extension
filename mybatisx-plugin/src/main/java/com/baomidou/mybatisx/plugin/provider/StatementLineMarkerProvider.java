package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.dom.model.Delete;
import com.baomidou.mybatisx.dom.model.IdDomElement;
import com.baomidou.mybatisx.dom.model.Insert;
import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.dom.model.Select;
import com.baomidou.mybatisx.dom.model.Update;
import com.baomidou.mybatisx.util.Icons;
import com.baomidou.mybatisx.util.JavaUtils;
import com.baomidou.mybatisx.util.MapperUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.ide.util.PsiElementListCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Set;

/**
 * The type Statement line marker provider.
 *
 * @author yanglin
 */
public class StatementLineMarkerProvider extends RelatedItemLineMarkerProvider {

  private static final String MAPPER_CLASS = Mapper.class.getSimpleName().toLowerCase();
  private static final Set<String> TARGET_TYPES = Set.of(
    Select.class.getSimpleName().toLowerCase(),
    Insert.class.getSimpleName().toLowerCase(),
    Update.class.getSimpleName().toLowerCase(),
    Delete.class.getSimpleName().toLowerCase()
  );

  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo<?>> result) {
    if (element instanceof XmlToken
        && isTargetType((XmlToken) element)
        && MapperUtils.isElementWithinMybatisFile(element)) {

      DomElement domElement = DomUtil.getDomElement(element);
      if (domElement == null) {
        return;
      }
      if (domElement instanceof IdDomElement) { // 方法
        JavaUtils.findMethods(element.getProject(),
            MapperUtils.getNamespace(domElement),
            MapperUtils.getId((IdDomElement) domElement))
          .ifPresent(psiMethods -> result.add(createLineMarkerInfo(element, psiMethods)));
      } else {
        XmlTag xmlTag = domElement.getXmlTag();
        if (xmlTag == null) {
          return;
        }
        String namespace = xmlTag.getAttributeValue("namespace");
        if (StringUtils.isEmpty(namespace)) {
          return;
        }
        JavaUtils.findClasses(element.getProject(), namespace)
          .ifPresent(psiClasses -> result.add(createLineMarkerInfo(element, psiClasses)));
      }
    }
  }

  private static class MapperFileListCellRender extends PsiElementListCellRenderer<PsiElement> {

    @Override
    public String getElementText(PsiElement element) {
      return element.getContainingFile().getName();
    }

    @Override
    protected String getContainerText(PsiElement element, String name) {
      return PsiUtils.getProjectRelativePath(element);
    }
  }

  private RelatedItemLineMarkerInfo<PsiElement> createLineMarkerInfo(PsiElement target, PsiElement[] elements) {
    return NavigationGutterIconBuilder.create(Icons.STATEMENT_LINE_MARKER_ICON)
      .setTooltipTitle("Navigation to Target in Mapper Client")
      .setTargets(elements)
      .setCellRenderer(MapperFileListCellRender::new)
      .createLineMarkerInfo(target);
  }


  private static boolean isTargetType(@NotNull XmlToken token) {
    Boolean targetType = null;
    if (MAPPER_CLASS.equals(token.getText())) {
      // 判断当前元素是开始节点
      PsiElement nextSibling = token.getNextSibling();
      if (nextSibling instanceof PsiWhiteSpace) {
        targetType = true;
      }
    }
    if (targetType == null) {
      if (TARGET_TYPES.contains(token.getText())) {
        PsiElement parent = token.getParent();
        // 判断当前节点是标签
        if (parent instanceof XmlTag) {
          // 判断当前元素是开始节点
          PsiElement nextSibling = token.getNextSibling();
          if (nextSibling instanceof PsiWhiteSpace) {
            targetType = true;
          }
        }
      }
    }
    if (targetType == null) {
      targetType = false;
    }
    return targetType;
  }

  @Override
  public @Nullable("null means disabled")
  String getName() {
    return "Statement line marker";
  }

  @NotNull
  @Override
  public Icon getIcon() {
    return Icons.MAPPER_LINE_MARKER_ICON;
  }

}
