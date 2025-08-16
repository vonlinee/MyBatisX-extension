package com.baomidou.mybatisx.plugin.setting.config;

import com.baomidou.mybatisx.dom.model.IdDomElement;
import com.baomidou.mybatisx.dom.model.Mapper;
import com.baomidou.mybatisx.plugin.extensions.ListSelectionListener;
import com.baomidou.mybatisx.plugin.ui.UiComponentFacade;
import com.baomidou.mybatisx.service.EditorService;
import com.baomidou.mybatisx.service.JavaService;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.JavaUtils;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors.CollectProcessor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * 抽象 Statement 代码生成器
 * </p>
 *
 * @author jobob
 * @since 2018 -07-30
 */
@Setter
@Getter
public abstract class AbstractStatementGenerator {

  private static final Function<Mapper, String> FUN = mapper -> {
    XmlTag xmlTag = mapper.getXmlTag();
    if (xmlTag == null) {
      return null;
    }
    VirtualFile vf = mapper.getXmlTag().getContainingFile().getVirtualFile();
    if (null == vf) {
      return "";
    }
    return vf.getCanonicalPath();
  };

  private Set<String> patterns;

  /**
   * Instantiates a new Abstract statement generator.
   *
   * @param patterns the patterns
   */
  public AbstractStatementGenerator(@NotNull String... patterns) {
    this.patterns = Sets.newHashSet(patterns);
  }

  /**
   * 获取方法的返回类型
   *
   * @param method the method
   * @return select result type
   */
  public static Optional<PsiClass> getSelectResultType(@Nullable PsiMethod method) {
    if (null == method) {
      return Optional.empty();
    }
    PsiType returnType = method.getReturnType();
    // 是基本类型, 并且不是 void
    if (returnType instanceof PsiPrimitiveType && !PsiType.VOID.equals(returnType)) {
      return JavaUtils.findClass(method.getProject(), Objects.requireNonNull(((PsiPrimitiveType) returnType).getBoxedTypeName()));
    } else if (returnType instanceof PsiClassReferenceType) {
      PsiClassReferenceType type = (PsiClassReferenceType) returnType;
      if (type.hasParameters()) {
        PsiType[] parameters = type.getParameters();
        // 处理是 List 的返回结果, 将List<T> 的泛型拿出来
        if (parameters.length == 1) {
          PsiType parameter = parameters[0];
          // 通常情况 List<?> 这里一定是引用类型, 但是进入这里的来源还有检查方法,
          // 不仅仅只是生成xml,  所以这里加一个判断.
          if (parameter instanceof PsiClassReferenceType) {
            type = (PsiClassReferenceType) parameter;
          }
        }
      }
      return Optional.ofNullable(type.resolve());
    }
    return Optional.empty();
  }

  /**
   * Apply generate.
   *
   * @param method  the method
   * @param project the project instance
   */
  public static void applyGenerate(@Nullable final PsiMethod method, Project project) {
    if (null == method) {
      return;
    }
    final AbstractStatementGenerator[] generators = getGenerators(method);
    if (1 == generators.length) {
      generators[0].execute(method, method.getProject());
    } else {
      BaseListPopupStep<AbstractStatementGenerator> step = new BaseListPopupStep<AbstractStatementGenerator>("[ Statement type for method: " + method.getName() + "]", generators) {
        @Override
        public PopupStep<?> onChosen(AbstractStatementGenerator selectedValue, boolean finalChoice) {
          return this.doFinalStep(() -> WriteCommandAction.writeCommandAction(project)
            .run(() -> selectedValue.execute(method, project)));
        }
      };
      JBPopupFactory.getInstance().createListPopup(step).showInFocusCenter();
    }
  }

  /**
   * Get generators abstract statement generator [ ].
   *
   * @param method the method
   * @return the abstract statement generator [ ]
   */
  @NotNull
  public static AbstractStatementGenerator[] getGenerators(@NotNull PsiMethod method) {
    String target = method.getName();
    List<AbstractStatementGenerator> result = Lists.newArrayList();
    for (AbstractStatementGenerator generator : StatementGenerators.ALL) {
      for (String pattern : generator.getPatterns()) {
        // 一定是以关键字开头
        if (target.startsWith(pattern)) {
          result.add(generator);
        }
      }
    }
    return CollectionUtils.isNotEmpty(result) ? result.toArray(new AbstractStatementGenerator[0]) : StatementGenerators.ALL.toArray(new AbstractStatementGenerator[0]);
  }

  /**
   * Execute.
   *
   * @param method  the method
   * @param project the project instance
   */
  public void execute(@NotNull final PsiMethod method, final Project project) {
    PsiClass psiClass = method.getContainingClass();
    if (null == psiClass) {
      return;
    }
    CollectProcessor<Mapper> processor = new CollectProcessor<>();
    JavaService.getInstance(project).processClass(psiClass, processor);
    final List<Mapper> mappers = Lists.newArrayList(processor.getResults());
    if (1 == mappers.size()) {
      setupTag(method, (Mapper) Iterables.getOnlyElement(mappers, (Object) null), project);
    } else if (mappers.size() > 1) {
      Collection<String> paths = Collections2.transform(mappers, FUN);
      UiComponentFacade.getInstance(project)
        .showListPopup("Choose target mapper xml to generate", new ListSelectionListener() {
          @Override
          public void selected(int index) {
            // 修复多模块生成标签, 修改xml内容不允许在用户线程操作的BUG
            WriteCommandAction.runWriteCommandAction(project, () -> setupTag(method, mappers.get(index), method.getProject()));
          }

          @Override
          public boolean isWriteAction() {
            return true;
          }
        }, paths.toArray(new String[0]));
    }
  }

  private void setupTag(PsiMethod method, Mapper mapper, Project project) {
    IdDomElement target = getTarget(mapper, method);
    target.getId().setStringValue(method.getName());
    target.setValue(" ");
    XmlTag tag = target.getXmlTag();
    assert tag != null;
    int offset = tag.getTextOffset() + tag.getTextLength() - tag.getName().length() + 1;
    EditorService editorService = EditorService.getInstance(project);
    editorService.format(tag.getContainingFile(), tag);
    editorService.scrollTo(tag, offset);
  }

  @Override
  public String toString() {
    return this.getDisplayText();
  }

  /**
   * Gets target.
   *
   * @param mapper the mapper
   * @param method the method
   * @return the target
   */
  @NotNull
  protected abstract IdDomElement getTarget(@NotNull Mapper mapper, @NotNull PsiMethod method);

  /**
   * Gets id.
   *
   * @return the id
   */
  @NotNull
  public abstract String getId();

  /**
   * Gets display text.
   *
   * @return the display text
   */
  @NotNull
  public abstract String getDisplayText();

}
