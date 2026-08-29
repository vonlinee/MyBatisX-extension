package com.baomidou.mybatisx.plugin.structure;

import com.baomidou.mybatisx.util.Icons;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.List;

class MyBatisMapperXmlStructureViewElement implements StructureViewTreeElement {

  private static final TreeElement[] EMPTY_CHILDREN = new TreeElement[0];

  private final PsiElement element;

  MyBatisMapperXmlStructureViewElement(@NotNull PsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return element;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (element instanceof Navigatable) {
      ((Navigatable) element).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return element instanceof Navigatable && ((Navigatable) element).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return element instanceof Navigatable && ((Navigatable) element).canNavigateToSource();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public @Nullable String getPresentableText() {
        return getElementText();
      }

      @Override
      public @Nullable String getLocationString() {
        return element instanceof XmlTag ? ((XmlTag) element).getName() : null;
      }

      @Override
      public @Nullable Icon getIcon(boolean unused) {
        return getElementIcon();
      }
    };
  }

  @Override
  public @NotNull TreeElement[] getChildren() {
    XmlTag mapperTag = getMapperTag();
    if (mapperTag == null) {
      return EMPTY_CHILDREN;
    }

    List<TreeElement> children = new ArrayList<>();
    for (XmlTag subTag : mapperTag.getSubTags()) {
      if (MyBatisMapperXmlStructureItemType.isSupportedTagName(subTag.getName())) {
        children.add(new MyBatisMapperXmlStructureViewElement(subTag));
      }
    }
    return children.toArray(EMPTY_CHILDREN);
  }

  private @Nullable XmlTag getMapperTag() {
    if (element instanceof XmlFile) {
      return ((XmlFile) element).getRootTag();
    }
    return null;
  }

  private @NlsSafe String getElementText() {
    if (element instanceof PsiFile) {
      return ((PsiFile) element).getName();
    }
    if (element instanceof XmlTag) {
      XmlTag tag = (XmlTag) element;
      String id = tag.getAttributeValue("id");
      return id == null || id.isBlank() ? tag.getName() : id;
    }
    return "";
  }

  private @Nullable Icon getElementIcon() {
    if (element instanceof XmlFile) {
      return Icons.MAPPER_XML_ICON;
    }
    if (!(element instanceof XmlTag)) {
      return null;
    }

    String tagName = ((XmlTag) element).getName();
    MyBatisMapperXmlStructureItemType itemType = MyBatisMapperXmlStructureItemType.fromTagName(tagName);
    if (itemType == MyBatisMapperXmlStructureItemType.STATEMENT) {
      return getStatementIcon(tagName);
    }
    return Icons.MAPPER_LINE_MARKER_ICON;
  }

  private @Nullable Icon getStatementIcon(@NotNull String tagName) {
    switch (tagName) {
      case "select":
        return Icons.STATEMENT_SELECT_ICON;
      case "insert":
        return Icons.STATEMENT_INSERT_ICON;
      case "update":
        return Icons.STATEMENT_UPDATE_ICON;
      case "delete":
        return Icons.STATEMENT_DELETE_ICON;
      default:
        return Icons.STATEMENT_LINE_MARKER_ICON;
    }
  }
}
