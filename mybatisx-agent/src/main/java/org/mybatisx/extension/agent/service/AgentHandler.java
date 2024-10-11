package org.mybatisx.extension.agent.service;

import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.AgentRequest;

public interface AgentHandler<T> {

    /**
     * 检查环境是否正确
     *
     * @return true表示正常，否则异常
     */
    boolean validate() throws AgentException;

    void dispatch(AgentRequest<T> command) throws AgentException;
}
