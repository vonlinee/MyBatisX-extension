package com.baomidou.mybatisx.plugin.listener;

import com.baomidou.mybatisx.agent.VMContext;
import com.baomidou.mybatisx.agent.VMInfo;
import com.baomidou.mybatisx.plugin.setting.MyBatisXSettings;
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
import com.sun.tools.attach.spi.AttachProvider;
import org.jetbrains.annotations.NotNull;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.Log;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AgentSupportExecutionListener implements ExecutionListener {

  private static final Pattern javaExeRegex = Pattern.compile("^(.*?)java.exe");
  private static final String[] runTypeList = new String[]{"application", "spring boot", "jar application"};
  private static final String AGENT_JAR = "mybatisx-agent.jar";
  private static String agentJarPath;

  static {
    try {
      agentJarPath = ProjectUtil.copyToLocal(AgentSupportExecutionListener.class.getResourceAsStream("/" + AGENT_JAR), AGENT_JAR);
    } catch (IOException e) {
      Log.error("failed to init agent.jar to local location", e);
    }
  }

  /**
   * tools.jar 会区分平台
   * 通过 SPI 加载 AttachProvider
   *
   * @param executorId executorId
   * @param env        env
   * @param handler    handler, the process
   */
  @Override
  public void processStarted(@NotNull String executorId, @NotNull ExecutionEnvironment env, @NotNull ProcessHandler handler) {
    if (agentJarPath == null) {
      return;
    }
    if (!MyBatisXSettings.getInstance().getState().hotswapEnabled) {
      return;
    }

    // Issue: https://youtrack.jetbrains.com/issue/IJPL-46869
    // https://plugins.jetbrains.com/docs/intellij/enabling-internal.html
    String property = System.getProperty("idea.is.internal");
    if ("true".equals(property)) {
      System.getProperties().remove("idea.is.internal");
    }

    List<AttachProvider> providers = AttachProvider.providers();
    if (providers.isEmpty()) {
      Log.error("no provider installed!");
      return;
    }

    RunProfile runProfile = env.getRunProfile();
    if (runProfile instanceof RunConfigurationBase) {
      RunConfigurationBase<?> runProfileBase = (RunConfigurationBase<?>) runProfile;
      if (!StringUtils.equalsAnyIgnoreCase(runProfileBase.getType().getDisplayName(), runTypeList)) {
        return;
      }
    }
    String runProfileName = runProfile.getName();
    String javaBinDir = getJavaBinDir(env, handler);
    final String pid;
    try {
      pid = ProjectUtil.getPid(javaBinDir, runProfileName);
    } catch (Throwable throwable) {
      Log.error(String.format("failed to get pid %s %s", javaBinDir, runProfileName), throwable);
      return;
    }
    try {
      int port = ProjectUtil.findAvailablePort();

      MyBatisXSettings.State state = MyBatisXSettings.getInstance().getState();
      state.port = String.valueOf(port);

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
