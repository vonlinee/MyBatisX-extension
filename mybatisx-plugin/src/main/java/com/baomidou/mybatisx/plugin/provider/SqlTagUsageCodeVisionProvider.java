package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.util.DomUtils;
import com.intellij.codeInsight.codeVision.CodeVisionAnchorKind;
import com.intellij.codeInsight.codeVision.CodeVisionEntry;
import com.intellij.codeInsight.codeVision.CodeVisionRelativeOrdering;
import com.intellij.codeInsight.codeVision.ui.model.ClickableTextCodeVisionEntry;
import com.intellij.codeInsight.hints.codeVision.ReferencesCodeVisionProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static kotlin.TuplesKt.to;

/**
 * Shows the usages of a MyBatis SQL fragment next to its {@code id} attribute.
 */
public final class SqlTagUsageCodeVisionProvider extends ReferencesCodeVisionProvider {

  private static final String PROVIDER_ID = "mybatisx.sql.references";

  @Override
  public boolean acceptsFile(@NotNull PsiFile file) {
    return file instanceof XmlFile && DomUtils.isMybatisFile(file);
  }

  @Override
  public boolean acceptsElement(@NotNull PsiElement element) {
    return isSqlIdValue(element);
  }

  @Override
  public @Nullable String getHint(@NotNull PsiElement element, @NotNull PsiFile file) {
    if (!acceptsFile(file) || !acceptsElement(element)) {
      return null;
    }
    return formatUsageHint(findUsageCount(element));
  }

  @Override
  public @NotNull List<Pair<TextRange, CodeVisionEntry>> computeForEditor(@NotNull Editor editor,
                                                                          @NotNull PsiFile file) {
    if (!acceptsFile(file)) {
      return Collections.emptyList();
    }

    List<Pair<TextRange, CodeVisionEntry>> result = new ArrayList<>();
    Collection<XmlAttributeValue> idValues = PsiTreeUtil.findChildrenOfType(
      file, XmlAttributeValue.class
    );
    for (XmlAttributeValue idValue : idValues) {
      if (!acceptsElement(idValue)) {
        continue;
      }
      String hint = formatUsageHint(findUsageCount(idValue));
      if (hint == null) {
        continue;
      }
      Function2<MouseEvent, Editor, Unit> clickHandler = (event, clickedEditor) -> {
        SqlTagUsageCodeVisionProvider.super.handleClick(clickedEditor, idValue, event);
        return Unit.INSTANCE;
      };
      CodeVisionEntry entry = new ClickableTextCodeVisionEntry(
        hint, // 展示在编辑器代码上方的文本
        getId(), // 当前 CodeVisionProvider 的唯一标识符
        clickHandler, // 回调函数，定义用户点击该文本时的行为。点击时的鼠标事件（MouseEvent，可能为 null）和当前的编辑器对象（Editor）
        null, // 文本左侧显示的图标。如果不想要图标，传入 null
        "5 references in project", // 当编辑器横向空间不足，或者在某些特殊长文本视图中展示的“完整/长版”文本。通常与 text 保持一致或更详细
        "Click to view all usages in this project", // 当鼠标悬停在 Code Vision 文本上时显示的气泡提示（Tooltip）
        Collections.emptyList() // 如果你希望用户右键该提示、或者在提示旁边显示一个小齿轮/下拉菜单来执行额外操作（如“隐藏此提示”、“配置...”），可以在这里传入操作列表。通常填 emptyList() 或直接不填。
      );
      result.add(to(idValue.getTextRange(), entry));
    }
    return result;
  }

  @Override
  public @NotNull List<CodeVisionRelativeOrdering> getRelativeOrderings() {
    return List.of(CodeVisionRelativeOrdering.CodeVisionRelativeOrderingFirst.INSTANCE);
  }

  @Override
  public @NotNull CodeVisionAnchorKind getDefaultAnchor() {
    return CodeVisionAnchorKind.Default;
  }

  @Override
  public @NotNull String getId() {
    return PROVIDER_ID;
  }

  static boolean isSqlIdValue(@NotNull PsiElement element) {
    if (!(element instanceof XmlAttributeValue)) {
      return false;
    }
    PsiElement parent = element.getParent();
    if (!(parent instanceof XmlAttribute) || !"id".equals(((XmlAttribute) parent).getName())) {
      return false;
    }
    PsiElement tag = parent.getParent();
    return tag != null && "sql".equals(((XmlTag) tag).getName());
  }

  @Nullable
  static String formatUsageHint(int usageCount) {
    if (usageCount <= 0) {
      return null;
    }
    return usageCount == 1 ? "1 usage" : usageCount + " usages";
  }

  private static int findUsageCount(@NotNull PsiElement sqlIdValue) {
    int count = 0;
    for (PsiReference ignored : ReferencesSearch.search(sqlIdValue).findAll()) {
      count++;
    }
    return count;
  }
}
