package com.baomidou.mybatisx.plugin.listener;

import com.baomidou.mybatisx.agent.VMContext;
import com.baomidou.mybatisx.agent.VMInfo;
import com.baomidou.mybatisx.util.ProjectUtil;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.execution.ExecutionListener;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.jetbrains.annotations.NotNull;
import org.mybatisx.extension.agent.AgentException;
import org.mybatisx.extension.agent.Log;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LoadAgentExecutionListener implements ExecutionListener {

    private static final Pattern javaExeRegex = Pattern.compile("^(.*?)java.exe");
    private static final String[] runTypeList = new String[]{"application", "spring boot", "jar application"};
    private static String agentJarPath;

    static {
        try {
            agentJarPath = ProjectUtil.copyToLocal(LoadAgentExecutionListener.class.getResourceAsStream("/mybatisx-agent.jar"), "mybatisx-agent.jar");
        } catch (IOException e) {
            Log.error("failed to init agent.jar to local location", e);
            System.exit(-1);
        }
    }

    @Override
    public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
        ExecutionListener.super.processStarted(executorId, env, handler);
        try {
            RunProfile runProfile = env.getRunProfile();
            if (runProfile instanceof RunConfigurationBase) {
                RunConfigurationBase<?> runProfileBase = (RunConfigurationBase<?>) runProfile;
                if (!StringUtils.equalsAnyIgnoreCase(runProfileBase.getType().getDisplayName(), runTypeList)) {
                    return;
                }
            }
            String runProfileName = runProfile.getName();
            String javaBinDir = getJavaBinDir(env, handler);
            String pid = ProjectUtil.getPid(javaBinDir, runProfileName);
            int port = ProjectUtil.findAvailablePort();
            VirtualMachine virtualMachine = VirtualMachine.attach(pid);

            VMInfo VMInfo = new VMInfo();
            VMInfo.setProcessName(runProfileName);
            VMInfo.setVirtualMachine(virtualMachine);
            VMInfo.setIp("127.0.0.1");
            VMInfo.setPort(port);
            VMInfo.setPid(pid);
            VMContext.put(runProfileName, VMInfo);
            virtualMachine.loadAgent(agentJarPath, port + "");
        } catch (AttachNotSupportedException | IOException | AgentException e) {
            Log.error("failed to attach %s %s", executorId, e.getMessage());
            throw new RuntimeException(e);
        } catch (AgentLoadException | AgentInitializationException e) {
            if (!e.getMessage().equals("0")) {
                Log.error("failed to attach %s %s", executorId, e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private String getJavaBinDir(ExecutionEnvironment env, ProcessHandler handler) {
        RunContentDescriptor contentToReuse = env.getContentToReuse();
        if (contentToReuse == null) {
            return null;
        }
        ProcessHandler processHandler = contentToReuse.getProcessHandler();
        if (processHandler == null) {
            return null;
        }
        String commandLine = processHandler.toString();
        Matcher matcher = javaExeRegex.matcher(commandLine);
        return matcher.find() ? matcher.group(1) : "";
    }

    @Override
    public void processTerminated(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler, int exitCode) {
        ExecutionListener.super.processTerminated(executorId, env, handler, exitCode);
        RunProfile runProfile = env.getRunProfile();
        if (runProfile instanceof RunConfigurationBase) {
            RunConfigurationBase<?> runProfileBase = (RunConfigurationBase<?>) env.getRunProfile();
            if (!StringUtils.equalsAnyIgnoreCase(runProfileBase.getType().getDisplayName(), runTypeList)) {
                return;
            }
        }

        final String runProfileName = runProfile.getName();
        VMInfo VMInfo = VMContext.get(runProfileName);
        if (VMInfo != null) {
            try {
                VMInfo.getVirtualMachine().detach();
            } catch (IOException e) {
                Log.error("failed to detach %s %s", executorId, e.getMessage());
                throw new RuntimeException(e);
            } finally {
                VMContext.remove(runProfileName);
            }
        }
    }
}
