package com.baomidou.mybatisx.plugin.resultmap;

import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class JavaClassTextGenerator {

  private JavaClassTextGenerator() {
  }

  public static String generate(ResultMapGenerationOptions options) {
    Set<String> imports = new LinkedHashSet<>();
    Map<String, String> importedTypes = new HashMap<>();
    for (SqlColumnModel column : options.getColumns()) {
      if (!column.isIgnored()) {
        addImport(imports, importedTypes, column.getJavaType());
      }
    }
    if (options.isUseLombok()) {
      imports.add("lombok.Data");
      if (options.isGenerateNoArgsConstructor()) {
        imports.add("lombok.NoArgsConstructor");
      }
    }
    if (options.isJpaEntity()) {
      String jpa = options.getJpaPackage();
      imports.add(jpa + ".Column");
      imports.add(jpa + ".Entity");
      imports.add(jpa + ".Table");
      if (hasId(options)) {
        imports.add(jpa + ".Id");
      }
      if (hasGeneratedId(options)) {
        imports.add(jpa + ".GeneratedValue");
        imports.add(jpa + ".GenerationType");
      }
    }

    StringBuilder text = new StringBuilder();
    if (options.getPackageName() != null && !options.getPackageName().isBlank()) {
      text.append("package ").append(options.getPackageName()).append(";\n\n");
    }
    for (String item : imports) {
      text.append("import ").append(item).append(";\n");
    }
    if (!imports.isEmpty()) {
      text.append('\n');
    }
    if (options.isJpaEntity()) {
      text.append("@Entity\n@Table(name = \"").append(options.getTableName()).append("\")\n");
    }
    if (options.isUseLombok()) {
      text.append("@Data\n");
      if (options.isGenerateNoArgsConstructor()) {
        text.append("@NoArgsConstructor\n");
      }
    }
    text.append("public class ").append(options.getClassName()).append(" {\n\n");
    for (SqlColumnModel column : options.getColumns()) {
      if (column.isIgnored()) {
        continue;
      }
      if (options.isJpaEntity()) {
        if (column.isId()) {
          text.append("  @Id\n");
        }
        if (column.isAutoIncrement()) {
          text.append("  @GeneratedValue(strategy = GenerationType.")
            .append(options.getGeneratedValueStrategy()).append(")\n");
        }
        text.append("  @Column(name = \"").append(column.getColumnName()).append("\")\n");
      }
      text.append("  private ").append(typeForField(column.getJavaType(), importedTypes)).append(" ")
        .append(column.getPropertyName()).append(";\n\n");
    }
    if (!options.isUseLombok() && options.isGenerateNoArgsConstructor()) {
      text.append("  public ").append(options.getClassName()).append("() {\n  }\n\n");
    }
    text.append("}\n");
    return text.toString();
  }

  private static boolean hasId(ResultMapGenerationOptions options) {
    return options.getColumns().stream().anyMatch(column -> column.isId() && !column.isIgnored());
  }

  private static boolean hasGeneratedId(ResultMapGenerationOptions options) {
    return options.getColumns().stream().anyMatch(column -> column.isId() && column.isAutoIncrement() && !column.isIgnored());
  }

  private static void addImport(Set<String> imports, Map<String, String> importedTypes, String type) {
    if (type == null || type.isBlank() || type.startsWith("java.lang.") || !type.contains(".")) {
      return;
    }
    String simple = NamingUtils.simpleName(type);
    if (importedTypes.containsKey(simple) && !importedTypes.get(simple).equals(type)) {
      String previous = importedTypes.get(simple);
      importedTypes.put(simple, null);
      imports.remove(previous);
      return;
    }
    importedTypes.put(simple, type);
    imports.add(type);
  }

  private static String typeForField(String type, Map<String, String> importedTypes) {
    if (type == null || type.isBlank() || !type.contains(".")) {
      return type == null || type.isBlank() ? "String" : type;
    }
    String simple = NamingUtils.simpleName(type);
    return importedTypes.containsKey(simple) && importedTypes.get(simple) == null ? type : simple;
  }

  static String shortType(String type) {
    return type == null || type.isBlank() ? "String" : NamingUtils.simpleName(type);
  }
}
