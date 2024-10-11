package org.mybatisx.extension.agent;

import org.mybatisx.extension.agent.api.Log;
import org.mybatisx.extension.agent.impl.MyBatisXTransformer;
import org.mybatisx.extension.agent.runtime.AgentContextHolder;
import org.mybatisx.extension.agent.server.AgentServer;

import java.lang.instrument.Instrumentation;

public class AgentMain {

    /**
     * attach回调方法
     *
     * @param agentArgs VirtualMachine#attach(pid)
     *                  VirtualMachine.loadAgent("xxx.jar", "agentArgs")
     * @param inst      Instrumentation
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        try {
            int port = Integer.parseInt(agentArgs);
            inst.addTransformer(new MyBatisXTransformer());
            AgentServer.start(port);
            AgentContextHolder.init(port, inst);
            Log.info("agent server is listening on port %s", agentArgs);
        } catch (RuntimeException e) {
            Log.error("failed to start agent server", e);
        }
    }
}
