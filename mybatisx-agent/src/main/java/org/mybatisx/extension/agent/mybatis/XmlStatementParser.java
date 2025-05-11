package org.mybatisx.extension.agent.mybatis;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;

public class XmlStatementParser {

  private final MapperStatementParser msParser;

  public XmlStatementParser() {
    this.msParser = new MapperStatementParser();
  }

  /**
   * 解析单个标签的XML语句
   *
   * @param xmlStatement 不包含 <sql> 标签等内容
   * @return MappedStatement实例
   */
  public MappedStatement parse(Configuration configuration, String resources, String namespace, String xmlStatement) {
    if (configuration == null) {
      throw new UnsupportedOperationException("configuration is not set");
    }
    MyMapperBuilderAssistant assistant = new MyMapperBuilderAssistant(configuration, resources);
    assistant.setResource(resources);
    assistant.setCurrentNamespace(namespace);
    MissingCompatiableStatementBuilder statementParser = new MissingCompatiableStatementBuilder(configuration, msParser.getNode(xmlStatement), assistant);
    return statementParser.parseMappedStatement();
  }
}
