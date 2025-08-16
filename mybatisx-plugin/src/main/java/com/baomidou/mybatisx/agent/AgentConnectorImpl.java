package com.baomidou.mybatisx.agent;

import org.mybatisx.extension.agent.api.AgentRequest;
import org.mybatisx.extension.agent.api.AgentResponse;
import org.mybatisx.extension.agent.api.MyBatisXAgent;
import org.mybatisx.extension.agent.api.RPCTarget;

public class AgentConnectorImpl<T, R> implements AgentConnector<T, R> {

  /**
   * 通过代理生成
   */
  @RPCTarget(value = "MyBatisXAgentImpl")
  private MyBatisXAgent<T, R> agent;

  @Override
  public AgentResponse<R> execute(AgentRequest<T> request) {
    return agent.execute(request);
  }
}
