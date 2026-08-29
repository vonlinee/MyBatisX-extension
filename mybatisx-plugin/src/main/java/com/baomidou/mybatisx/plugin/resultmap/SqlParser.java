package com.baomidou.mybatisx.plugin.resultmap;

import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

/**
 * SQL parsing extension point used by resultMap generation.
 *
 * <p>The default implementation deliberately does not execute SQL. A project
 * can replace it later with a richer IntelliJ SQL PSI implementation.</p>
 */
public interface SqlParser {

  @NotNull
  SqlParseResult parse(@NotNull Project project, @NotNull XmlTag selectTag);
}
