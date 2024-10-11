package com.baomidou.mybatisx.agent;

import com.baomidou.mybatisx.util.Notifications;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.client.TargetProxy;

import java.lang.reflect.Field;
import java.util.Collections;

public class JavaFileHandler implements Handler {

    private final AgentConnector<org.mybatisx.extension.agent.api.JavaClassHotSwapDTO, Object> connector = TargetProxy.getProxy(new AgentConnectorImpl<>());

    @Override
    public boolean supports(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        return ((String) obj).toLowerCase().endsWith(".java");
    }

    @Override
    public void execute(Object obj) throws org.mybatisx.extension.agent.api.AgentException {
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
            org.mybatisx.extension.agent.api.JavaClassHotSwapDTO dto = new org.mybatisx.extension.agent.api.JavaClassHotSwapDTO(file.getPath());
            org.mybatisx.extension.agent.api.AgentRequest<org.mybatisx.extension.agent.api.JavaClassHotSwapDTO> command = new org.mybatisx.extension.agent.api.AgentRequest<>(org.mybatisx.extension.agent.api.AgentCommandEnum.JAVA_CLASS_HOTSWAP, dto);

            final String processName = e.getPresentation().getText();
            connector.sendRequest(Collections.singletonList(VMContext.get(processName)), vm -> {
                org.mybatisx.extension.agent.api.AgentResponse<Object> agentResponse = connector.execute(command);
                Notifications.notify("[" + vm.getProcessName() + "]:" + agentResponse.getMsg(), agentResponse.isOk() ? NotificationType.INFORMATION : NotificationType.ERROR);
            });
        } catch (Exception exception) {
            throw new AgentException(exception.getMessage(), exception);
        }
    }
}
