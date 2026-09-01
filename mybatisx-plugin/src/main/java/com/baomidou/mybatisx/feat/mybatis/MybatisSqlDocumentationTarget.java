package com.baomidou.mybatisx.feat.mybatis;

import com.intellij.model.Pointer;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.editor.richcopy.HtmlSyntaxInfoUtil;
import com.intellij.platform.backend.documentation.DocumentationResult;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.presentation.TargetPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class MybatisSqlDocumentationTarget implements DocumentationTarget {

  private final PsiElement myTargetElement;

  public MybatisSqlDocumentationTarget(@NotNull PsiElement targetElement) {
    this.myTargetElement = targetElement;
  }

  @Override
  public @NotNull Pointer<? extends DocumentationTarget> createPointer() {
    Pointer<PsiElement> elementPointer = SmartPointerManager.createPointer(myTargetElement);
    return () -> {
      PsiElement element = elementPointer.dereference();
      return element != null ? new MybatisSqlDocumentationTarget(element) : null;
    };
  }

  @Override
  public @NotNull TargetPresentation computePresentation() {
    // 获取 <sql> 的 id 作为展示名字
    String name = "SQL Fragment";
    if (myTargetElement instanceof XmlAttributeValue) {
      name = ((XmlAttributeValue) myTargetElement).getValue();
    }
    // 构建目标元素的呈现样式
    return TargetPresentation.builder(name)
      .icon(PlatformIcons.XML_TAG_ICON) // 使用平台自带的 XML 标签图标
      .locationText(myTargetElement.getContainingFile().getName()) // 显示所属文件名
      .presentation();
  }

  @Override
  public @Nullable DocumentationResult computeDocumentation() {
    XmlTag sqlTag = findSqlTag(myTargetElement);

    if (sqlTag != null && "sql".equals(sqlTag.getName())) {
      String sqlId = sqlTag.getAttributeValue("id");
      String sqlContent = sqlTag.getValue().getText().trim();
      return DocumentationResult.documentation(buildDocumentationHtml(sqlTag.getProject(), sqlId, sqlContent));
    }

    return null;
  }

  @Nullable
  static XmlTag findSqlTag(@NotNull PsiElement element) {
    if (element instanceof XmlTag) {
      return (XmlTag) element;
    }
    if (!(element instanceof XmlAttributeValue)) {
      return null;
    }

    PsiElement parent = element.getParent();
    if (!(parent instanceof XmlAttribute) || parent.getParent() == null) {
      return null;
    }
    return (XmlTag) parent.getParent();
  }

  static String buildDocumentationHtml(@NotNull com.intellij.openapi.project.Project project,
                                        @Nullable String sqlId,
                                        @Nullable String sqlContent) {
    String content = sqlContent == null ? "" : sqlContent;
    StringBuilder html = new StringBuilder("<html>");
    html.append(DocumentationMarkup.DEFINITION_START)
      .append("<b>SQL Fragment:</b> <code>")
      .append(escapeHtml(sqlId))
      .append("</code>")
      .append(DocumentationMarkup.DEFINITION_END)
      .append(DocumentationMarkup.CONTENT_START)
      .append("<pre class='code'>");

    try {
      HtmlSyntaxInfoUtil.appendHighlightedByLexerAndEncodedAsHtmlCodeSnippet(
        html, project, XMLLanguage.INSTANCE, content, true, 1.0f
      );
    } catch (RuntimeException ignored) {
      // Syntax highlighting is best-effort; malformed XML still needs to be readable.
      html.append(escapeHtml(content));
    }

    html.append("</pre>")
      .append(DocumentationMarkup.CONTENT_END)
      .append("</html>");
    return html.toString();
  }

  private static String escapeHtml(@Nullable String text) {
    if (text == null) {
      return "";
    }
    return text.replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace("'", "&#39;");
  }
}
