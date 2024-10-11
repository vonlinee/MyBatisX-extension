package org.mybatisx.extension.agent.api;

public interface MyBatisXAgent<T, R> {

    /**
     * 执行指令
     *
     * @param agentRequest AgentCommand
     * @return 响应结果
     */
    AgentResponse<R> execute(AgentRequest<T> agentRequest);
}
