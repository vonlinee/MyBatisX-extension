package com.baomidou.mybatisx.plugin.resultmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SqlParseResult {

  private final List<SqlColumnModel> columns = new ArrayList<>();
  private final Set<String> tables = new LinkedHashSet<>();
  private final List<String> warnings = new ArrayList<>();

  public List<SqlColumnModel> getColumns() {
    return columns;
  }

  public Set<String> getTables() {
    return Collections.unmodifiableSet(tables);
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void addTable(String table) {
    if (table != null && !table.isBlank()) {
      tables.add(table);
    }
  }

  public void addWarning(String warning) {
    if (warning != null && !warning.isBlank()) {
      warnings.add(warning);
    }
  }
}
