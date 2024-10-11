package org.mybatisx.extension.agent.api;

import java.io.Serializable;

public class AgentRequest<T> implements Serializable {

    private AgentCommandEnum commandEnum;

    private T data;

    public AgentRequest() {
    }

    public AgentRequest(AgentCommandEnum commandEnum, T data) {
        this.commandEnum = commandEnum;
        this.data = data;
    }

    public AgentCommandEnum getCommandEnum() {
        return commandEnum;
    }

    public void setCommandEnum(AgentCommandEnum commandEnum) {
        this.commandEnum = commandEnum;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
