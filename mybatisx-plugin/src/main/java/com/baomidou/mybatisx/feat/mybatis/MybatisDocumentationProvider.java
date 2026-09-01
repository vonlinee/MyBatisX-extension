package com.baomidou.mybatisx.feat.mybatis;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.Nullable;

/**
 * 需要确保 IDEA 开启了鼠标悬浮展示文档的功能。在 IDEA 运行时，引导用户检查以下设置：
 * 打开 Settings / Preferences -> Editor -> General。
 * 勾选 Show quick documentation on hover（在鼠标悬浮时显示快速文档）。
 *
 * 效果说明当你把鼠标悬停在 <include refid="xxxx"> 的 "xxxx" 上时，IDE 会通过 MybatisIncludeReference.resolve() 顺藤摸瓜找到目标 <sql id="xxxx">。随后 MybatisDocumentationProvider 介入，提取该 <sql> 标签内的实际 SQL 文本，通过 HTML <pre> 标签保持换行和缩进，最终渲染在一个优雅的悬浮气泡框中。
 */
@Deprecated
public class MybatisDocumentationProvider extends AbstractDocumentationProvider {

  @Nullable
  @Override
  public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
    // element 是经过你引用的 resolve() 方法解析后返回的目标对象

    // 如果 resolve() 返回的是 id 属性值，先获取它的父级标签 <sql>
    XmlTag sqlTag = null;
    if (element instanceof XmlAttributeValue) {
      PsiElement parent = element.getParent(); // XmlAttribute
      if (parent != null && parent.getParent() instanceof XmlTag) {
        sqlTag = (XmlTag) parent.getParent();
      }
    } else if (element instanceof XmlTag) {
      sqlTag = (XmlTag) element;
    }

    // 确保找到了 <sql> 标签
    if (sqlTag != null && "sql".equals(sqlTag.getName())) {
      String sqlId = sqlTag.getAttributeValue("id");

      // 获取 <sql> 标签内部的文本/SQL语句（去掉外层的 <sql id="..."> 壳子）
      // 如果想连带标签一起展示，可以直接用 sqlTag.getText()
      String sqlContent = sqlTag.getValue().getText().trim();

      // 转义 HTML 特殊字符，防止 SQL 中的 < 或 > 破坏浮窗排版
      String escapedSql = escapeHtml(sqlContent);

      // 拼装符合 IDE 风格的 HTML 浮窗内容
      return "<html>" +
             "<b>SQL Fragment:</b> <code>" + sqlId + "</code>" +
             "<hr/>" +
             "<pre style='font-family: monospace;'>" + escapedSql + "</pre>" +
             "</html>";
    }

    return null;
  }

  private String escapeHtml(String text) {
    if (text == null) return "";
    return text.replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace("'", "&#37;");
  }
}
