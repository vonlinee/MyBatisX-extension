package com.baomidou.mybatisx.alias;

import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The type Inner alias resolver.
 *
 * @author yanglin
 */
public class InnerAliasResolver extends AliasResolver {
  private static final Logger logger = LoggerFactory.getLogger(InnerAliasResolver.class);
  private volatile Set<AliasDesc> innerAliasDescriptions = null;

  /**
   * Instantiates a new Inner alias resolver.
   *
   * @param project the project
   */
  public InnerAliasResolver(Project project) {
    super(project);
  }

  private Set<AliasDesc> getAliasDescSet() {
    Set<AliasDesc> aliasDescriptions = new HashSet<>();
    addAliasDesc(aliasDescriptions, "java.lang.String", "string");
    addAliasDesc(aliasDescriptions, "java.lang.Byte", "byte");
    addAliasDesc(aliasDescriptions, "java.lang.Long", "long");
    addAliasDesc(aliasDescriptions, "java.lang.Short", "short");
    addAliasDesc(aliasDescriptions, "java.lang.Integer", "int");
    addAliasDesc(aliasDescriptions, "java.lang.Integer", "integer");
    addAliasDesc(aliasDescriptions, "java.lang.Double", "double");
    addAliasDesc(aliasDescriptions, "java.lang.Float", "float");
    addAliasDesc(aliasDescriptions, "java.lang.Boolean", "boolean");
    addAliasDesc(aliasDescriptions, "java.util.Date", "date");
    addAliasDesc(aliasDescriptions, "java.math.BigDecimal", "decimal");
    addAliasDesc(aliasDescriptions, "java.lang.Object", "object");
    addAliasDesc(aliasDescriptions, "java.util.Map", "map");
    addAliasDesc(aliasDescriptions, "java.util.HashMap", "hashmap");
    addAliasDesc(aliasDescriptions, "java.util.List", "list");
    addAliasDesc(aliasDescriptions, "java.util.ArrayList", "arraylist");
    addAliasDesc(aliasDescriptions, "java.util.Collection", "collection");
    addAliasDesc(aliasDescriptions, "java.util.Iterator", "iterator");
    return aliasDescriptions;
  }

  private void addAliasDesc(Set<AliasDesc> aliasDescs, String clazz, String alias) {
    Optional<PsiClass> psiClassOptional = JavaUtils.findClass(project, clazz);
    if (psiClassOptional.isPresent()) {
      PsiClass psiClass = psiClassOptional.get();
      AliasDesc aliasDesc = AliasDesc.create(psiClass, alias);
      aliasDescs.add(aliasDesc);
    } else {
      logger.error("无法找到别名映射, class: {}, alias: {}", clazz, alias);
    }

  }

  /**
   * 支持延迟识别, 当项目第一次打开时，可能未配置JDK， 在未配置JDK时， 内部别名无法注册。
   * 这里支持等手动配置JDK后才开始缓存
   *
   * @param element the element
   * @return
   */
  @NotNull
  @Override
  public Set<AliasDesc> getClassAliasDescriptions(@Nullable PsiElement element) {
    if (innerAliasDescriptions == null) {
      Set<AliasDesc> aliasDescSet = getAliasDescSet();
      if (!aliasDescSet.isEmpty()) {
        synchronized (this) {
          this.innerAliasDescriptions = aliasDescSet;
        }
      }
    }
    return Objects.requireNonNullElse(innerAliasDescriptions, Collections.emptySet());
  }
}
