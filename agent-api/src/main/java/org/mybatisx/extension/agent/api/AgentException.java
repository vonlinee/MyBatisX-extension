package org.mybatisx.extension.agent.api;

public class AgentException extends RuntimeException {

    public AgentException(String errorMsg) {
        super(errorMsg);
    }

    public AgentException(String errorMsg, Object... args) {
        super(String.format(errorMsg, args));
    }

    public AgentException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }
}
