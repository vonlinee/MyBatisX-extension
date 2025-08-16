package org.mybatisx.extension.agent.service;

import org.apache.ibatis.mapping.MappedStatement;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.AgentRequest;
import org.mybatisx.extension.agent.api.MapperHotSwapDTO;
import org.mybatisx.extension.agent.mybatis.XmlStatementParser;

public class MyBatisMappedStatementAgentHandler<T> implements AgentHandler<T> {

  public MyBatisMappedStatementAgentHandler() {
  }

  @Override
  public boolean validate() throws AgentException {
    return true;
  }

  /**
   * com.baomidou.mybatisplus.core.MybatisConfiguration#addMappedStatement
   * <p>
   *
   * @param command command
   */
  @Override
  public void dispatch(AgentRequest<T> command) throws AgentException {
    MapperHotSwapDTO dto = (MapperHotSwapDTO) command.getData();
    MappedStatement mappedStatement;
    try {
      mappedStatement = XmlStatementParser.parse(MyBatisContext.getConfiguration(), dto.getMapperXmlPath(), dto.getNamespace(), dto.getContent());
    } catch (Throwable throwable) {
      throw new AgentException(String.format("failed to hotswap %s parse error", dto.getNamespace()), throwable);
    }
    if (mappedStatement != null) {
      try {
        MyBatisContext.replace(mappedStatement.getId(), mappedStatement);
      } catch (Throwable throwable) {
        throw new AgentException(String.format("failed to hotswap %s", mappedStatement.getId()), throwable);
      }
    }
  }
}
