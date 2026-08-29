package com.baomidou.mybatisx.plugin.structure;

import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MyBatisMapperXmlStructureItemTypeTest {

  @Test
  public void shouldRecognizeNavigableMyBatisMapperTags() {
    assertEquals(MyBatisMapperXmlStructureItemType.RESULT_MAP,
      MyBatisMapperXmlStructureItemType.fromTagName("resultMap"));
    assertEquals(MyBatisMapperXmlStructureItemType.STATEMENT,
      MyBatisMapperXmlStructureItemType.fromTagName("select"));
    assertEquals(MyBatisMapperXmlStructureItemType.STATEMENT,
      MyBatisMapperXmlStructureItemType.fromTagName("insert"));
    assertEquals(MyBatisMapperXmlStructureItemType.STATEMENT,
      MyBatisMapperXmlStructureItemType.fromTagName("update"));
    assertEquals(MyBatisMapperXmlStructureItemType.STATEMENT,
      MyBatisMapperXmlStructureItemType.fromTagName("delete"));
  }

  @Test
  public void shouldIgnoreNonStructureTags() {
    assertFalse(MyBatisMapperXmlStructureItemType.isSupportedTagName("mapper"));
    assertFalse(MyBatisMapperXmlStructureItemType.isSupportedTagName("sql"));
    assertFalse(MyBatisMapperXmlStructureItemType.isSupportedTagName("if"));
    assertFalse(MyBatisMapperXmlStructureItemType.isSupportedTagName("where"));
    assertFalse(MyBatisMapperXmlStructureItemType.isSupportedTagName(null));
  }

  @Test
  public void shouldExposeStatementTags() {
    assertTrue(MyBatisMapperXmlStructureItemType.STATEMENT.matches("select"));
    assertTrue(MyBatisMapperXmlStructureItemType.STATEMENT.matches("insert"));
    assertTrue(MyBatisMapperXmlStructureItemType.STATEMENT.matches("update"));
    assertTrue(MyBatisMapperXmlStructureItemType.STATEMENT.matches("delete"));
    assertFalse(MyBatisMapperXmlStructureItemType.STATEMENT.matches("sql"));
  }

  @Test
  public void shouldResolveStatementIconsByTagName() {
    assertEquals("/icons/statement_select.svg",
      MyBatisMapperXmlStructureItemType.getStatementIconPath("select"));
    assertEquals("/icons/statement_insert.svg",
      MyBatisMapperXmlStructureItemType.getStatementIconPath("insert"));
    assertEquals("/icons/statement_update.svg",
      MyBatisMapperXmlStructureItemType.getStatementIconPath("update"));
    assertEquals("/icons/statement_delete.svg",
      MyBatisMapperXmlStructureItemType.getStatementIconPath("delete"));
  }

  @Test
  public void shouldRenderFullStatementTextInIcons() throws Exception {
    assertStatementIconContainsText("select", "SELECT");
    assertStatementIconContainsText("insert", "INSERT");
    assertStatementIconContainsText("update", "UPDATE");
    assertStatementIconContainsText("delete", "DELETE");
  }

  private void assertStatementIconContainsText(String tagName, String text) throws Exception {
    try (InputStream inputStream = getClass().getResourceAsStream(
      MyBatisMapperXmlStructureItemType.getStatementIconPath(tagName))) {
      assertNotNull("Missing icon resource for " + tagName, inputStream);
      String svg = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      assertTrue("Icon should contain full text " + text, svg.contains(">" + text + "<"));
    }
  }
}
