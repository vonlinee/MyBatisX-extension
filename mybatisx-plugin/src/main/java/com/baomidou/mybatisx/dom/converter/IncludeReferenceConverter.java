package com.baomidou.mybatisx.dom.converter;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.GenericDomValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 1. 在 XML 中点击 <include refid="xxxx"> 内部的文本，按住 Ctrl (或 Cmd)，文本会变成超链接。点击即可直接精确跳转到 <sql id="xxxx"> 的位置。
 * 2. 在 refid="" 内按下 Ctrl + Space，会自动弹出当前文件内所有 sql id 的补全列表。
 * 另一种实现方式是: PsiReferenceContributor
 */
public class IncludeReferenceConverter extends ConverterAdaptor<XmlTag> implements CustomReferenceConverter<XmlTag> {

  /**
   *
   * @param psiElement refid 属性
   * @param convertContext context
   * @return 引用
   */
  @NotNull
  @Override
  public PsiReference[] createReferences(GenericDomValue<XmlTag> genericDomValue, PsiElement psiElement, ConvertContext convertContext) {
    // 1. 确保底层的 PsiElement 是 XML 属性值类型
    if (psiElement instanceof XmlAttributeValue) {
      // 2. 直接实例化你之前写的 Reference 并返回
      return new PsiReference[]{new MybatisIncludeReference((XmlAttributeValue) psiElement)};
    }
    // Idea 中会飘红, 并且提示: Cannot resolve symbol 'Example_Where_Clause'
    return PsiReference.EMPTY_ARRAY;
  }

  /**
   * 处理跳转逻辑
   */
  public static class MybatisIncludeReference extends PsiReferenceBase<XmlAttributeValue> {

    // 传入的 element 是 <include refid="xxxx"> 中的 "xxxx"（XmlAttributeValue）
    public MybatisIncludeReference(@NotNull XmlAttributeValue element) {
      super(element, new TextRange(1, element.getTextLength() - 1)); // 去掉两侧的引号
    }

    /**
     *
     * @return 返回该引用所指向的 PsiElement（定义处）。如果找不到，返回 null
     */
    @Nullable
    @Override
    public PsiElement resolve() {
      String refId = myElement.getValue();
      if (refId.isEmpty()) return null;
      // 获取当前的 XML 文件
      PsiElement file = myElement.getContainingFile();
      if (file == null) return null;

      // 查找文件内所有的 <sql> 标签
      Collection<XmlTag> xmlTags = PsiTreeUtil.findChildrenOfType(file, XmlTag.class);
      for (XmlTag tag : xmlTags) {
        if ("sql".equals(tag.getName())) {
          String sqlId = tag.getAttributeValue("id");
          if (refId.equals(sqlId)) {
            // 找到匹配的 id，返回该 id 属性值对象，以便精确跳转
            XmlAttribute idAttr = tag.getAttribute("id");
            if (idAttr != null && idAttr.getValueElement() != null) {
              return idAttr.getValueElement();
            }
            return tag;
          }
        }
      }
      return null;
    }

    /**
     * 用于代码补全（Code Completion）。
     * @return 返回一个数组，包含当前上下文中所有可能的建议选项。
     */
    @NotNull
    @Override
    public Object[] getVariants() {
      // 用于代码自动补全：收集当前文件所有 <sql> 的 id
      List<String> variants = new ArrayList<>();
      PsiElement file = myElement.getContainingFile();
      if (file != null) {
        Collection<XmlTag> xmlTags = PsiTreeUtil.findChildrenOfType(file, XmlTag.class);
        for (XmlTag tag : xmlTags) {
          if ("sql".equals(tag.getName())) {
            String sqlId = tag.getAttributeValue("id");
            if (sqlId != null && !sqlId.isEmpty()) {
              variants.add(sqlId);
            }
          }
        }
      }
      return variants.toArray();
    }
  }
}
