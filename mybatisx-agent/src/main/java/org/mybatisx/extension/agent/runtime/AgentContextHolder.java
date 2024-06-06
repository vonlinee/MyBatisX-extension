package org.mybatisx.extension.agent.runtime;

import java.lang.instrument.Instrumentation;

public class AgentContextHolder {

    private static final AgentContextHolder agentContext = new AgentContextHolder();

    private int port;

    private Instrumentation inst;

    AgentContextHolder() {
    }

    public static void init(int port, Instrumentation inst) {
        agentContext.port = port;
        agentContext.inst = inst;
    }

    public static AgentContextHolder getInstance() {
        return agentContext;
    }

    public int getPort() {
        return port;
    }

    public Instrumentation getInst() {
        return inst;
    }
}
