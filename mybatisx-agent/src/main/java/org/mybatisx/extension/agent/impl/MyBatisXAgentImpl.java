package org.mybatisx.extension.agent.impl;

import org.mybatisx.extension.agent.api.AgentCommandEnum;
import org.mybatisx.extension.agent.api.AgentRequest;
import org.mybatisx.extension.agent.api.AgentResponse;
import org.mybatisx.extension.agent.api.MyBatisXAgent;
import org.mybatisx.extension.agent.service.AgentHandler;
import org.mybatisx.extension.agent.service.JavaClassHotSwapAgentHandler;
import org.mybatisx.extension.agent.service.MyBatisMappedStatementAgentHandler;
import org.mybatisx.extension.agent.service.MyBatisMapperXmlHotSwapAgentHandler;

import java.util.HashMap;
import java.util.Map;

public class MyBatisXAgentImpl<T, R> implements MyBatisXAgent<T, R> {

  private static final Map<AgentCommandEnum, AgentHandler<?>> HANDLER_MAP = new HashMap<>();

  static {
    HANDLER_MAP.put(AgentCommandEnum.MYBATIS_MAPPER_FILE_HOTSWAP, new MyBatisMapperXmlHotSwapAgentHandler<>());
    HANDLER_MAP.put(AgentCommandEnum.JAVA_CLASS_HOTSWAP, new JavaClassHotSwapAgentHandler<>());
    HANDLER_MAP.put(AgentCommandEnum.MYBATIS_MAPPER_STATEMENT_HOTSWAP, new MyBatisMappedStatementAgentHandler<>());
  }

  @Override
  public AgentResponse<R> execute(AgentRequest<T> agentRequest) {
    @SuppressWarnings("unchecked")
    AgentHandler<T> handler = (AgentHandler<T>) HANDLER_MAP.get(agentRequest.getCommandEnum());
    if (handler == null) {
      return AgentResponse.failed("cannot find handler: " + agentRequest.getCommandEnum(), null);
    }
    try {
      handler.dispatch(agentRequest);
      return AgentResponse.success("success", null);
    } catch (Exception e) {
      return AgentResponse.failed("failed to execute " + e.getMessage(), null);
    }
  }
}
