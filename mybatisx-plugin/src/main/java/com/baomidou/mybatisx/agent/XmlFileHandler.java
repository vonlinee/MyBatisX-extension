package com.baomidou.mybatisx.agent;

import com.baomidou.mybatisx.util.Notifications;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.client.TargetProxy;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlFileHandler implements Handler {

    private static final String mapperClassRegex = "<mapper\\s+namespace\\s*=\\s*\"(.+)\">";
    private final AgentConnector<org.mybatisx.extension.agent.api.MapperHotSwapDTO, Object> connector = TargetProxy.getProxy(new AgentConnectorImpl<>());

    @Override
    public boolean supports(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }
        return ((String) obj).toLowerCase().endsWith(".xml");
    }

    @Override
    public void execute(Object obj) throws AgentException {
        AnActionEvent e = (AnActionEvent) obj;
        PsiFile psiFile = e.getData(PlatformDataKeys.PSI_FILE);
        if (psiFile == null) {
            return;
        }
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }
        Document document = editor.getDocument();
        String documentText = document.getText();
        Matcher matcher = Pattern.compile(mapperClassRegex).matcher(documentText);

        if (matcher.find()) {
            String mapperClass = matcher.group(1).replace("\\s", "");

            org.mybatisx.extension.agent.api.MapperHotSwapDTO dto = new org.mybatisx.extension.agent.api.MapperHotSwapDTO();
            dto.setMapperClass(mapperClass);
            dto.setMapperXmlPath(psiFile.getVirtualFile().getPath());

            org.mybatisx.extension.agent.api.AgentRequest<org.mybatisx.extension.agent.api.MapperHotSwapDTO> command = new org.mybatisx.extension.agent.api.AgentRequest<>(org.mybatisx.extension.agent.api.AgentCommandEnum.MYBATIS_MAPPER_FILE_HOTSWAP, dto);

            // Run Configuration 名称
            String runConfigurationName = e.getPresentation().getText();
            connector.sendRequest(Collections.singletonList(VMContext.get(runConfigurationName)), vm -> {
                org.mybatisx.extension.agent.api.AgentResponse<Object> response = connector.execute(command);
                String msg = "[" + vm.getProcessName() + "]:" + response.getMsg();
                Notifications.notify(msg, response.isOk() ? NotificationType.INFORMATION : NotificationType.ERROR);
            });
        } else {
            Notifications.warning("failed to parse namespace");
        }
    }
}
