package com.baomidou.plugin.idea.mybatisx.provider;

import com.baomidou.plugin.idea.mybatisx.util.Icons;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.ui.popup.PopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mybatisx.extension.agent.AgentRequest;
import org.mybatisx.extension.agent.AgentCommandEnum;
import org.mybatisx.extension.agent.AgentResponse;
import org.mybatisx.extension.agent.MapperHotSwapDTO;
import org.mybatisx.extension.agent.client.TargetProxy;
import com.baomidou.plugin.idea.mybatisx.agent.connect.AgentConnector;
import com.baomidou.plugin.idea.mybatisx.agent.connect.AgentConnectorImpl;
import com.baomidou.plugin.idea.mybatisx.agent.context.VMInfo;
import com.baomidou.plugin.idea.mybatisx.agent.context.VMContext;
import com.baomidou.plugin.idea.mybatisx.agent.util.Notifications;

import javax.swing.*;
import java.util.Collections;

/**
 * 只更新单个Mapper Statement
 */
public class HotSwapNavigator implements MapperNavigator {

    private final AgentConnector<MapperHotSwapDTO, Object> connector;

    public HotSwapNavigator() {
        this.connector = TargetProxy.getProxy(new AgentConnectorImpl<>());
    }

    @Override
    public Icon getIcon() {
        return Icons.GUTTER_HOT_SWAP_ICON;
    }

    @Override
    public String getDisplayText() {
        return "HotSwap";
    }

    @Override
    public void navigate(Project project, MapperStatementItem item) {
        int count = VMContext.size();
        if (count == 0) {
            Notifications.notify("No running process", NotificationType.ERROR);
        } else if (count == 1) {
            VMContext.first().ifPresent(vmInfo -> hotswap(project, item, vmInfo));
        } else if (count > 1) {
            VMInfo[] vmInfos = VMContext.values().toArray(new VMInfo[]{});
            ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(new BaseListPopupStep<VMInfo>("Running Profiles", vmInfos) {

                @Override
                public @NotNull String getTextFor(VMInfo vmInfo) {
                    return vmInfo.getIp() + ":" + vmInfo.getPort() + " " + vmInfo.getPid() + " " + vmInfo.getProcessName();
                }

                @Override
                public @Nullable PopupStep<?> onChosen(VMInfo selectedValue, boolean finalChoice) {
                    hotswap(project, item, selectedValue);
                    return PopupStep.FINAL_CHOICE;
                }
            });
            listPopup.show(new RelativePoint(item.getOriginEvent()));
        }
    }

    public void hotswap(Project project, MapperStatementItem item, VMInfo vmInfo) {

        MapperHotSwapDTO mapperHotswapDto = new MapperHotSwapDTO();
        mapperHotswapDto.setMapperXmlPath(item.getMapperXmlFileLocation());
        mapperHotswapDto.setNamespace(item.getNamespace());
        mapperHotswapDto.setContent(item.getContent());

        AgentRequest<MapperHotSwapDTO> command = new AgentRequest<>(AgentCommandEnum.MYBATIS_MAPPER_STATEMENT_HOTSWAP, mapperHotswapDto);
        try {
            connector.sendRequest(Collections.singletonList(vmInfo), vm -> {
                AgentResponse<Object> agentResponse = connector.execute(command);
                Notifications.notify("[" + vm.getProcessName() + "]:" + agentResponse.getMsg(), agentResponse.isOk() ? NotificationType.INFORMATION : NotificationType.ERROR);
            });
        } catch (Throwable throwable) {
            Notifications.notify("failed to hotswap " + throwable.getMessage(), NotificationType.ERROR);
        }
    }

    @Override
    public String getNavigationGroupName() {
        return null;
    }
}
