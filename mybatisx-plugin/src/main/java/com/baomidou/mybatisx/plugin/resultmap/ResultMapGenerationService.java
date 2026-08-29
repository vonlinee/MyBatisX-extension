package com.baomidou.mybatisx.plugin.resultmap;

import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Shared orchestration for Structure View and XML intention entry points.
 */
public final class ResultMapGenerationService {

  private final SqlParser sqlParser;

  public ResultMapGenerationService() {
    this(new DefaultSqlParser());
  }

  public ResultMapGenerationService(@NotNull SqlParser sqlParser) {
    this.sqlParser = sqlParser;
  }

  public void showDialog(@NotNull Project project, @NotNull XmlTag selectTag) {
    if (!"select".equalsIgnoreCase(selectTag.getName())) {
      return;
    }
    ResultMapGenerationOptions options = prepare(project, selectTag);
    if (options.getColumns().isEmpty()) {
      Messages.showWarningDialog(project, "No result columns could be inferred from this select.", "Generate Result Mapping");
      return;
    }
    ResultMapGenerationDialog dialog = new ResultMapGenerationDialog(project, options);
    dialog.show();
    if (dialog.isOK()) {
      apply(project, selectTag, dialog.getOptions());
    }
  }

  @NotNull
  ResultMapGenerationOptions prepare(@NotNull Project project, @NotNull XmlTag selectTag) {
    ResultMapGenerationOptions options = new ResultMapGenerationOptions();
    XmlFile xmlFile = (XmlFile) selectTag.getContainingFile();
    XmlTag root = xmlFile.getRootTag();
    String selectId = selectTag.getAttributeValue("id");
    String resultType = selectTag.getAttributeValue("resultType");
    String resultMapId = selectTag.getAttributeValue("resultMap");
    XmlTag existingResultMap = findResultMap(root, resultMapId);

    options.setResultMapId(resultMapId == null || resultMapId.isBlank()
                           ? uniqueResultMapId(root, selectId)
                           : resultMapId);
    options.setResultType(resultType);
    options.setGenerateJavaClass(resultType == null || resultType.isBlank());

    if (existingResultMap != null) {
      options.getColumns().addAll(ResultMapParser.parse(existingResultMap));
      for (XmlTag child : existingResultMap.getSubTags()) {
        String childName = child.getName();
        if ("association".equals(childName) || "collection".equals(childName) || "discriminator".equals(childName)) {
          options.getWarnings().add("Nested " + childName + " mappings are kept unchanged and are not expanded.");
        }
      }
      String mapType = existingResultMap.getAttributeValue("type");
      if ((resultType == null || resultType.isBlank()) && mapType != null) {
        options.setClassName(NamingUtils.simpleName(mapType));
        options.setPackageName(NamingUtils.packageName(mapType));
      }
    } else {
      SqlParseResult parseResult = sqlParser.parse(project, selectTag);
      options.getColumns().addAll(parseResult.getColumns());
      options.getWarnings().addAll(parseResult.getWarnings());
      options.setTableName(parseResult.getTables().stream().findFirst().orElse(NamingUtils.toPropertyName(selectId) + "_result"));
    }

    if (options.getClassName() == null || options.getClassName().isBlank()) {
      String type = resultType;
      if (type == null || type.isBlank()) {
        type = root == null ? null : root.getAttributeValue("namespace");
        options.setClassName(NamingUtils.classNameFromId(selectId));
      } else {
        options.setClassName(NamingUtils.simpleName(type));
      }
      options.setPackageName(type == null || type.isBlank()
                             ? ""
                             : NamingUtils.packageName(type));
    }
    if (options.getTableName() == null || options.getTableName().isBlank()) {
      options.setTableName(NamingUtils.toPropertyName(selectId) + "_result");
    }
    options.setTargetDirectory(findDefaultSourceDirectory(project, xmlFile));
    options.setJpaPackage(JavaUtils.findClass(project, "jakarta.persistence.Entity").isPresent()
                          ? "jakarta.persistence"
                          : "javax.persistence");
    return options;
  }

  public void apply(@NotNull Project project, @NotNull XmlTag selectTag, @NotNull ResultMapGenerationOptions options) {
    if (options.isGenerateJavaClass() && options.getTargetDirectory() == null) {
      Messages.showErrorDialog(project, "Please select a Java source directory.", "Generate Result Mapping");
      return;
    }
    WriteCommandAction.runWriteCommandAction(project, "Generate MyBatis Result Mapping", "MyBatisX", () -> {
      try {
        if (options.isGenerateJavaClass()) {
          writeJavaClass(project, options);
        }
        if (options.getResultType() == null || options.getResultType().isBlank()) {
          options.setResultType(ResultMapTextGenerator.qualifiedClassName(options));
        }
        XmlTag root = ((XmlFile) selectTag.getContainingFile()).getRootTag();
        if (!hasResultMap(root, options.getResultMapId())) {
          XmlTag resultMap = com.intellij.psi.XmlElementFactory.getInstance(project)
            .createTagFromText(ResultMapTextGenerator.generate(options));
          PsiElement anchor = findInsertionAnchor(root, selectTag);
          root.addBefore(createWhitespace(project, "\n\n"), anchor);
          root.addBefore(resultMap, anchor);
          root.addBefore(createWhitespace(project, "\n\n"), anchor);
        }
        XmlTag existing = findResultMap(root, options.getResultMapId());
        if (existing != null && existing.getAttributeValue("type") == null
            && options.getResultType() != null && !options.getResultType().isBlank()) {
          existing.setAttribute("type", options.getResultType());
        }
        if (selectTag.getAttribute("resultMap") == null) {
          selectTag.setAttribute("resultMap", options.getResultMapId());
        } else {
          selectTag.setAttribute("resultMap", options.getResultMapId());
        }
        if (selectTag.getAttribute("resultType") != null) {
          selectTag.getAttribute("resultType").delete();
        }
      } catch (IncorrectOperationException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void writeJavaClass(Project project, ResultMapGenerationOptions options) {
    PsiDirectory directory = ensurePackageDirectory(options.getTargetDirectory(), options.getPackageName());
    if (directory == null) {
      throw new IllegalStateException("Unable to resolve Java target directory.");
    }
    String fileName = options.getClassName() + ".java";
    PsiFile current = directory.findFile(fileName);
    if (current != null) {
      int choice = Messages.showYesNoCancelDialog(project,
        "Java class " + fileName + " already exists.\nYes: overwrite, No: merge new fields, Cancel: abort.",
        "Generate Result Mapping", Messages.getQuestionIcon());
      if (choice == Messages.CANCEL) {
        throw new IncorrectOperationException("Generation cancelled");
      }
      if (choice == Messages.NO) {
        mergeJavaClass(project, current, options);
        return;
      }
      current.delete();
    }
    PsiFile generated = PsiFileFactory.getInstance(project)
      .createFileFromText(fileName, JavaFileType.INSTANCE, JavaClassTextGenerator.generate(options));
    directory.add(generated);
  }

  private void mergeJavaClass(Project project, PsiFile current, ResultMapGenerationOptions options) {
    PsiFile additions = PsiFileFactory.getInstance(project)
      .createFileFromText("Generated.java", JavaFileType.INSTANCE, JavaClassTextGenerator.generate(options));
    if (!(current instanceof com.intellij.psi.PsiJavaFile) || !(additions instanceof com.intellij.psi.PsiJavaFile)) {
      return;
    }
    com.intellij.psi.PsiClass target = ((com.intellij.psi.PsiJavaFile) current).getClasses()[0];
    com.intellij.psi.PsiClass source = ((com.intellij.psi.PsiJavaFile) additions).getClasses()[0];
    for (com.intellij.psi.PsiField field : source.getFields()) {
      if (target.findFieldByName(field.getName(), false) == null) {
        target.add(field);
      }
    }
  }

  @Nullable
  private static PsiDirectory ensurePackageDirectory(@Nullable PsiDirectory source, String packageName) {
    if (source == null) {
      return null;
    }
    PsiDirectory directory = source;
    if (packageName == null || packageName.isBlank()) {
      return directory;
    }
    for (String segment : packageName.split("\\.")) {
      if (segment.isBlank()) {
        continue;
      }
      PsiDirectory child = directory.findSubdirectory(segment);
      directory = child != null ? child : directory.createSubdirectory(segment);
    }
    return directory;
  }

  @Nullable
  private static PsiDirectory findDefaultSourceDirectory(Project project, PsiFile xmlFile) {
    ProjectFileIndex index = ProjectRootManager.getInstance(project).getFileIndex();
    VirtualFile root = index.getSourceRootForFile(xmlFile.getVirtualFile());
    if (root != null && root.getName().toLowerCase(Locale.ROOT).contains("resource")) {
      VirtualFile javaRoot = root.getParent() == null ? null : root.getParent().findChild("java");
      if (javaRoot != null) {
        return PsiManager.getInstance(project).findDirectory(javaRoot);
      }
    }
    for (VirtualFile sourceRoot : ProjectRootManager.getInstance(project)
      .getContentRoots()) {
      VirtualFile java = findChild(sourceRoot, "src", "main", "java");
      if (java != null) {
        return PsiManager.getInstance(project).findDirectory(java);
      }
    }
    return null;
  }

  @Nullable
  private static VirtualFile findChild(VirtualFile root, String... path) {
    VirtualFile current = root;
    for (String item : path) {
      if (current == null) {
        return null;
      }
      current = current.findChild(item);
    }
    return current;
  }

  @Nullable
  private static XmlTag findResultMap(@Nullable XmlTag root, @Nullable String id) {
    if (root == null || id == null || id.isBlank()) {
      return null;
    }
    for (XmlTag tag : root.findSubTags("resultMap")) {
      if (Objects.equals(id, tag.getAttributeValue("id"))) {
        return tag;
      }
    }
    return null;
  }

  private static boolean hasResultMap(@Nullable XmlTag root, String id) {
    return findResultMap(root, id) != null;
  }

  private static String uniqueResultMapId(@Nullable XmlTag root, String selectId) {
    String base = (selectId == null || selectId.isBlank() ? "result" : selectId) + "ResultMap";
    if (!hasResultMap(root, base)) {
      return base;
    }
    int suffix = 2;
    while (hasResultMap(root, base + suffix)) {
      suffix++;
    }
    return base + suffix;
  }

  private static PsiElement findInsertionAnchor(@Nullable XmlTag root, XmlTag selectTag) {
    if (root == null) {
      return selectTag;
    }
    return selectTag;
  }

  private static PsiElement createWhitespace(Project project, String text) {
    return com.intellij.psi.PsiParserFacade.getInstance(project).createWhiteSpaceFromText(text);
  }
}
