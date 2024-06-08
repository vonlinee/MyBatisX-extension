package com.baomidou.mybatisx.agent;

import org.mybatisx.extension.agent.AgentException;
import org.mybatisx.extension.agent.AgentRequest;
import org.mybatisx.extension.agent.AgentResponse;
import org.mybatisx.extension.agent.Log;

import java.util.Collection;
import java.util.function.Consumer;

public interface AgentConnector<T, R> {

    AgentResponse<R> execute(AgentRequest<T> request);

    /**
     * 发送请求给AgentServer
     *
     * @param vmList   接收进程
     * @param consumer IoUtil
     * @throws AgentException HotSwapException
     */
    default void sendRequest(Collection<VMInfo> vmList, Consumer<VMInfo> consumer) throws AgentException {
        if (vmList != null && !vmList.isEmpty()) {
            try {
                vmList.forEach(vm -> {
                    try {
                        consumer.accept(vm);
                    } catch (Exception e) {
                        Log.error("vm failed to send command, process[%s] pid[%s], cause: %s", vm.getProcessName(), vm.getPid(), e.getMessage());
                    }
                });
            } catch (Exception e) {
                throw new AgentException("failed to send request", e);
            }
        }
    }
}
