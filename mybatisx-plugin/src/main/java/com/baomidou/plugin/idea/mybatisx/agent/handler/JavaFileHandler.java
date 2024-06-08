package com.baomidou.plugin.idea.mybatisx.agent.handler;

import com.baomidou.plugin.idea.mybatisx.agent.connect.AgentConnectorImpl;
import com.baomidou.plugin.idea.mybatisx.agent.context.VMContext;
import com.baomidou.plugin.idea.mybatisx.util.Notifications;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.mybatisx.extension.agent.*;
import com.baomidou.plugin.idea.mybatisx.agent.connect.AgentConnector;
import org.mybatisx.extension.agent.client.TargetProxy;

import java.lang.reflect.Field;
import java.util.Collections;

public class JavaFileHandler implements Handler {

    private final AgentConnector<JavaClassHotSwapDTO, Object> connector = TargetProxy.getProxy(new AgentConnectorImpl<>());

    @Override
    public boolean isSupport(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        return ((String) obj).toLowerCase().endsWith(".java");
    }

    @Override
    public void execute(Object obj) throws AgentException {
        try {
            AnActionEvent e = (AnActionEvent) obj;
            VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
            if (file == null) {
                return;
            }
            // 获取命名空间
            PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
            if (psiFile == null) {
                return;
            }
            Class<?> clazz = psiFile.getClass().getSuperclass();
            Field packageNameField = clazz.getDeclaredField("myPackageName");
            String packageName = (String) packageNameField.get(psiFile);
            JavaClassHotSwapDTO dto = new JavaClassHotSwapDTO(file.getPath());
            AgentRequest<JavaClassHotSwapDTO> command = new AgentRequest<>(AgentCommandEnum.JAVA_CLASS_HOTSWAP, dto);

            final String processName = e.getPresentation().getText();
            connector.sendRequest(Collections.singletonList(VMContext.get(processName)), vm -> {
                AgentResponse<Object> agentResponse = connector.execute(command);
                Notifications.notify("[" + vm.getProcessName() + "]:" + agentResponse.getMsg(), agentResponse.isOk() ? NotificationType.INFORMATION : NotificationType.ERROR);
            });
        } catch (Exception exception) {
            throw new AgentException(exception.getMessage(), exception);
        }
    }
}
