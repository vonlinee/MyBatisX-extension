package com.baomidou.mybatisx.plugin.resultmap;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public interface SqlMetadataProvider {

  @NotNull
  List<SqlColumnModel> findColumns(@NotNull Project project, @NotNull Set<String> tableNames);
}
