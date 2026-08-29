package com.baomidou.mybatisx.plugin.structure;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

enum MyBatisMapperXmlStructureItemType {
  RESULT_MAP("resultMap"),
  SQL("sql"),
  STATEMENT("select", "insert", "update", "delete");

  private final Set<String> tagNames;

  MyBatisMapperXmlStructureItemType(String... tagNames) {
    this.tagNames = new HashSet<>(Arrays.asList(tagNames));
  }

  static MyBatisMapperXmlStructureItemType fromTagName(String tagName) {
    for (MyBatisMapperXmlStructureItemType itemType : values()) {
      if (itemType.matches(tagName)) {
        return itemType;
      }
    }
    return null;
  }

  static boolean isSupportedTagName(String tagName) {
    return fromTagName(tagName) != null;
  }

  static String getStatementIconPath(String tagName) {
    if (!STATEMENT.matches(tagName)) {
      return null;
    }
    return "/icons/statement_" + tagName + ".svg";
  }

  boolean matches(String tagName) {
    return tagNames.contains(tagName);
  }
}
