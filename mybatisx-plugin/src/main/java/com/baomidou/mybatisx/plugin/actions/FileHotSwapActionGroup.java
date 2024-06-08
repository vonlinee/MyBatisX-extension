package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.agent.VMInfo;
import com.baomidou.mybatisx.agent.VMContext;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FileHotSwapActionGroup extends ActionGroup {
    @Override
    public AnAction @NotNull [] getChildren(@Nullable AnActionEvent e) {
        List<AnAction> actions = new ArrayList<>();
        for (VMInfo process : VMContext.values()) {
            actions.add(new FileHotSwapAction(process.getProcessName()));
        }
        return actions.toArray(new AnAction[0]);
    }
}
