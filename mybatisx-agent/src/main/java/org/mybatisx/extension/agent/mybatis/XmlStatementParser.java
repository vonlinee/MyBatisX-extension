package org.mybatisx.extension.agent.mybatis;

import org.apache.ibatis.builder.Configuration;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class XmlStatementParser {

  /**
   * 解析单个标签的XML语句
   *
   * @param xmlStatement 不包含 <sql> 标签等内容
   * @return MappedStatement实例
   */
  public static MappedStatement parse(Configuration configuration, String resources, String namespace, String xmlStatement) {
    if (configuration == null) {
      throw new UnsupportedOperationException("configuration is not set");
    }
    XPathParser parser = new XPathParser(xmlStatement);
    XNode root = parser.evalNode("/*");

    MapperBuilderAssistant assistant = new MapperBuilderAssistant(configuration, resources);
    assistant.setCurrentNamespace(namespace);
    XMLStatementBuilder builder = new XMLStatementBuilder(configuration, assistant, namespace) {
      @Override
      @SuppressWarnings("unchecked")
      protected @Nullable <T> Class<T> resolveClass(String alias) {
        try {
          return super.resolveClass(alias);
        } catch (Throwable throwable) {
          return (Class<T>) Object.class;
        }
      }

      @Override
      @SuppressWarnings("unchecked")
      protected <T> Class<? extends T> resolveAlias(String alias) {
        try {
          return super.resolveAlias(alias);
        } catch (Throwable throwable) {
          return (Class<? extends T>) Object.class;
        }
      }
    };
    return builder.parseStatementNode(root);
  }

  /**
   * @throws RuntimeException XML内容异常
   */
  public static String getString(String filepath, String id) {
    Configuration configuration = new Configuration();
    File file = new File(filepath);
    try (InputStream inputStream = new FileInputStream(file)) {
      XPathParser parser = new XPathParser(inputStream, true, configuration.getVariables(), new XMLMapperEntityResolver());
      XNode root = parser.evalRootNode();
      String namespace = root.getStringAttribute("namespace");
      List<XNode> sqlNodes = parser.evalNodes("/mapper/sql");
      Map<String, XNode> sqlFragments = new HashMap<>();
      for (XNode sqlNode : sqlNodes) {
        sqlFragments.put(namespace + "." + sqlNode.getStringAttribute("id"), sqlNode);
      }

      List<XNode> nodes = root.evalNodes(
        String.format("//select[@id='%s'] | //update[@id='%s'] | //insert[@id='%s'] | //delete[@id='%s']", id, id, id, id));

      XNode xNode = nodes.get(0);
      XMLIncludeTransformer transformer = new XMLIncludeTransformer();
      transformer.applyIncludes(namespace, xNode.getNode(), new Properties(), sqlFragments);
      return xNode.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
