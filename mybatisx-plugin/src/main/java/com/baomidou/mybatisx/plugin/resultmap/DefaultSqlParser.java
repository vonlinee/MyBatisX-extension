package com.baomidou.mybatisx.plugin.resultmap;

import com.baomidou.mybatisx.plugin.intention.MappedStatementTextBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dependency-light SQL parser. XML dynamic tags and &lt;include&gt; fragments
 * are expanded through the existing IntelliJ PSI visitor before parsing.
 */
public class DefaultSqlParser implements SqlParser {

  private static final Pattern ALIAS_PATTERN = Pattern.compile(
    "(?is)^(.*?)(?:\\s+as\\s+|\\s+)([a-zA-Z_][\\w$]*)$");
  private static final Pattern TABLE_PATTERN = Pattern.compile(
    "(?is)(?:from|join)\\s+([`\"\\w.$]+)(?:\\s+(?:as\\s+)?([`\"\\w$]+))?");
  private final SqlMetadataProvider metadataProvider;

  public DefaultSqlParser() {
    this(createMetadataProvider());
  }

  public DefaultSqlParser(@NotNull SqlMetadataProvider metadataProvider) {
    this.metadataProvider = metadataProvider;
  }

  private static SqlMetadataProvider createMetadataProvider() {
    try {
      Class.forName("com.intellij.database.psi.DbPsiFacade", false, DefaultSqlParser.class.getClassLoader());
      return new IntellijSqlMetadataProvider();
    } catch (ClassNotFoundException ignored) {
      return (project, tableNames) -> List.of();
    }
  }

  @Override
  public @NotNull SqlParseResult parse(@NotNull Project project, @NotNull XmlTag selectTag) {
    String sql = MappedStatementTextBuilder.build(selectTag);
    sql = cleanup(sql);
    SqlParseResult result = new SqlParseResult();
    if (sql.isBlank()) {
      result.addWarning("The select SQL is empty or only contains dynamic conditions.");
      return result;
    }

    Matcher tableMatcher = TABLE_PATTERN.matcher(sql);
    while (tableMatcher.find()) {
      result.addTable(unquote(tableMatcher.group(1)));
    }

    String selectList = extractSelectList(sql);
    if (selectList == null) {
      result.addWarning("Unable to locate the SELECT list.");
      return result;
    }

    List<String> expressions = splitTopLevel(selectList);
    Set<String> properties = new HashSet<>();
    for (String expression : expressions) {
      if ("*".equals(expression.trim()) || expression.trim().endsWith(".*")) {
        List<SqlColumnModel> metadata = metadataProvider.findColumns(project, result.getTables());
        if (metadata.isEmpty()) {
          result.addWarning("SELECT * cannot be expanded without an IntelliJ data source.");
        } else {
          for (SqlColumnModel column : metadata) {
            if (!properties.add(column.getPropertyName())) {
              column.setWarning("Duplicate property name; review the mapping.");
              result.addWarning("Duplicate result property: " + column.getPropertyName());
            }
            result.getColumns().add(column);
          }
        }
      } else {
        addColumn(result, expression, properties);
      }
    }
    if (result.getColumns().isEmpty()) {
      result.addWarning("No result columns could be inferred from the SELECT list.");
    }
    return result;
  }

  private static void addColumn(SqlParseResult result, String expression, Set<String> properties) {
    String item = expression.trim();
    if (item.isBlank()) {
      return;
    }
    if ("*".equals(item) || item.endsWith(".*")) {
      result.addWarning("SELECT * cannot be expanded without an IntelliJ data source.");
      return;
    }

    String source = item;
    String alias = null;
    Matcher aliasMatcher = ALIAS_PATTERN.matcher(item);
    if (aliasMatcher.matches() && !looksLikeFunction(item)) {
      String candidate = aliasMatcher.group(2);
      if (!isSqlKeyword(candidate)) {
        source = aliasMatcher.group(1).trim();
        alias = unquote(candidate);
      }
    }

    String columnName = alias != null ? alias : sourceColumnName(source);
    if (columnName.isBlank()) {
      result.addWarning("Unable to infer a column name from: " + item);
      return;
    }
    SqlColumnModel column = new SqlColumnModel(columnName);
    column.setPropertyName(NamingUtils.toPropertyName(columnName));
    column.setInferredJavaType(inferJavaType(source, columnName));
    column.setJdbcType(inferJdbcType(source, columnName));
    if (isLikelyId(columnName)) {
      column.setId(true);
    }
    if (!properties.add(column.getPropertyName())) {
      column.setWarning("Duplicate property name; review the mapping.");
      result.addWarning("Duplicate result property: " + column.getPropertyName());
    }
    result.getColumns().add(column);
  }

  private static String cleanup(String sql) {
    return sql.replaceAll("#\\{[^}]+}", "?")
      .replaceAll("\\$\\{[^}]+}", "?")
      .replaceAll("\\s+", " ")
      .trim();
  }

  private static String extractSelectList(String sql) {
    String lower = sql.toLowerCase(Locale.ROOT);
    int select = lower.indexOf("select");
    if (select < 0) {
      return null;
    }
    int from = findTopLevelKeyword(lower, "from", select + 6);
    if (from < 0) {
      return null;
    }
    return sql.substring(select + 6, from).trim();
  }

  private static int findTopLevelKeyword(String text, String keyword, int start) {
    int depth = 0;
    char quote = 0;
    for (int i = start; i <= text.length() - keyword.length(); i++) {
      char ch = text.charAt(i);
      if (quote != 0) {
        if (ch == quote && (i == 0 || text.charAt(i - 1) != '\\')) {
          quote = 0;
        }
        continue;
      }
      if (ch == '\'' || ch == '"' || ch == '`') {
        quote = ch;
      } else if (ch == '(') {
        depth++;
      } else if (ch == ')') {
        depth = Math.max(0, depth - 1);
      } else if (depth == 0 && text.regionMatches(true, i, keyword, 0, keyword.length())
                 && isBoundary(text, i - 1) && isBoundary(text, i + keyword.length())) {
        return i;
      }
    }
    return -1;
  }

  private static boolean isBoundary(String text, int index) {
    return index < 0 || index >= text.length() || !Character.isJavaIdentifierPart(text.charAt(index));
  }

  private static List<String> splitTopLevel(String value) {
    List<String> result = new ArrayList<>();
    int start = 0;
    int depth = 0;
    char quote = 0;
    for (int i = 0; i < value.length(); i++) {
      char ch = value.charAt(i);
      if (quote != 0) {
        if (ch == quote && (i == 0 || value.charAt(i - 1) != '\\')) {
          quote = 0;
        }
      } else if (ch == '\'' || ch == '"' || ch == '`') {
        quote = ch;
      } else if (ch == '(') {
        depth++;
      } else if (ch == ')') {
        depth = Math.max(0, depth - 1);
      } else if (ch == ',' && depth == 0) {
        result.add(value.substring(start, i));
        start = i + 1;
      }
    }
    result.add(value.substring(start));
    return result;
  }

  private static String sourceColumnName(String source) {
    String value = source.trim();
    int dot = value.lastIndexOf('.');
    if (dot >= 0) {
      value = value.substring(dot + 1);
    }
    int space = value.lastIndexOf(' ');
    if (space >= 0) {
      value = value.substring(space + 1);
    }
    return unquote(value.replaceAll("[^\\w$]", ""));
  }

  private static String unquote(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("`", "").replace("\"", "").trim();
  }

  private static boolean looksLikeFunction(String value) {
    return value.contains("(") && value.contains(")");
  }

  private static boolean isSqlKeyword(String value) {
    return Set.of("from", "where", "group", "order", "limit", "join", "on", "and", "or").contains(value.toLowerCase(Locale.ROOT));
  }

  private static boolean isLikelyId(String value) {
    String normalized = value.toLowerCase(Locale.ROOT);
    return normalized.equals("id") || normalized.endsWith("_id") || normalized.endsWith("id");
  }

  private static String inferJavaType(String source, String columnName) {
    String value = (source + " " + columnName).toLowerCase(Locale.ROOT);
    if (value.contains("count(") || value.contains("sum(") || value.contains("bigint") || value.contains("long")) {
      return "java.lang.Long";
    }
    if (value.contains("decimal") || value.contains("numeric") || value.contains("amount") || value.contains("price")) {
      return "java.math.BigDecimal";
    }
    if (value.contains("date") && !value.contains("datetime")) {
      return "java.time.LocalDate";
    }
    if (value.contains("time")) {
      return "java.time.LocalDateTime";
    }
    if (value.contains("bool")) {
      return "java.lang.Boolean";
    }
    if (value.contains("int")) {
      return "java.lang.Integer";
    }
    return "java.lang.String";
  }

  private static String inferJdbcType(String source, String columnName) {
    String value = (source + " " + columnName).toLowerCase(Locale.ROOT);
    if (value.contains("bigint") || value.contains("count(") || value.contains("sum(")) {
      return "BIGINT";
    }
    if (value.contains("decimal") || value.contains("numeric")) {
      return "DECIMAL";
    }
    if (value.contains("date") && !value.contains("datetime")) {
      return "DATE";
    }
    if (value.contains("time")) {
      return "TIMESTAMP";
    }
    if (value.contains("bool")) {
      return "BOOLEAN";
    }
    if (value.contains("int")) {
      return "INTEGER";
    }
    return "VARCHAR";
  }
}
