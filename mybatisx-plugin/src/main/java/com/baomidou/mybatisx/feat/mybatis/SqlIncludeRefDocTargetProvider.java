package com.baomidou.mybatisx.feat.mybatis;

import com.intellij.platform.backend.documentation.DocumentationTarget;
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class SqlIncludeRefDocTargetProvider implements PsiDocumentationTargetProvider {

  /**
   * @see com.baomidou.mybatisx.dom.model.Include
   * @param element 由 <include/> 元素触发
   * @return 文档目标列表
   */
  @Override
  public @NotNull List<DocumentationTarget> documentationTargets(@NotNull PsiElement element,
                                                                 @Nullable PsiElement originalElement) {
    if (element instanceof XmlAttributeValue) {
      PsiElement parent = element.getParent();
      // 检查这个对象是不是我们需要生成文档的 <sql id="..."> 处的属性值
      if (parent instanceof XmlAttribute && "id".equals(((XmlAttribute) parent).getName())) {
        PsiElement grandParent = parent.getParent();
        // 这里是 <sql> 标签，而不是 <include/> 标签
        // 参考: com.baomidou.mybatisx.dom.converter.SqlConverter
        if (grandParent != null && "sql".equals(((XmlTag) grandParent).getName())) {
          // 3. 确认为目标节点，包装返回
          return List.of(new MybatisSqlDocumentationTarget(element));
        }
      }
    }
    return Collections.emptyList();
  }
}
