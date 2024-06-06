package com.baomidou.plugin.idea.mybatisx.agent.connect;

import org.mybatisx.extension.agent.AgentRequest;
import org.mybatisx.extension.agent.AgentResponse;
import org.mybatisx.extension.agent.MyBatisXAgent;
import org.mybatisx.extension.agent.RPCTarget;

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
