package com.baomidou.mybatisx.plugin.structure;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.ide.structureView.xml.XmlStructureViewBuilderProvider;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MyBatisMapperXmlStructureViewFactory implements PsiStructureViewFactory, XmlStructureViewBuilderProvider {

  @Override
  public @Nullable StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
    if (!isMyBatisMapperXml(psiFile)) {
      return null;
    }
    return new MyBatisMapperXmlStructureViewBuilder(psiFile);
  }

  @Override
  public @Nullable StructureViewBuilder createStructureViewBuilder(@NotNull XmlFile xmlFile) {
    return getStructureViewBuilder(xmlFile);
  }

  static boolean isMyBatisMapperXml(@Nullable PsiFile psiFile) {
    if (!(psiFile instanceof XmlFile)) {
      return false;
    }
    XmlTag rootTag = ((XmlFile) psiFile).getRootTag();
    return rootTag != null
      && "mapper".equals(rootTag.getName())
      && rootTag.getAttribute("namespace") != null;
  }

  private static class MyBatisMapperXmlStructureViewBuilder extends TreeBasedStructureViewBuilder {

    private final PsiFile psiFile;

    private MyBatisMapperXmlStructureViewBuilder(@NotNull PsiFile psiFile) {
      this.psiFile = psiFile;
    }

    @Override
    public @NotNull StructureViewModel createStructureViewModel(@Nullable Editor editor) {
      return new StructureViewModelBase(psiFile, editor, new MyBatisMapperXmlStructureViewElement(psiFile))
        .withSuitableClasses(XmlTag.class);
    }

    @Override
    public boolean isRootNodeShown() {
      return true;
    }
  }
}
