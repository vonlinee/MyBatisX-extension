package com.baomidou.plugin.idea.mybatisx.provider;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class MapperXmlGutterIconRenderer extends LineMarkerInfo.LineMarkerGutterIconRenderer<PsiElement> {

    @NotNull
    MapperLineMarkerInfo info;

    AnAction leftAction;
    AnAction rightAction;

    public MapperXmlGutterIconRenderer(@NotNull MapperLineMarkerInfo lineMarkerInfo) {
        super(lineMarkerInfo);
        this.info = lineMarkerInfo;
    }

    /**
     * 左键单击进行文件跳转
     *
     * @return AnAction
     */
    @Override
    public @Nullable AnAction getClickAction() {
        if (leftAction == null) {
            leftAction = new AnAction() {
                @Override
                public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                    PsiElement target = info.getTargets()[0];
                    PsiFile targetFile = target.getContainingFile();
                    if (targetFile.canNavigate()) {
                        targetFile.navigate(true);
                    }
                }
            };
        }
        return leftAction;
    }

    /**
     * 右键打开弹窗
     *
     * @return AnAction
     */
    @Override
    public @Nullable AnAction getRightButtonClickAction() {
        if (rightAction == null) {
            rightAction = new AnAction() {
                @Override
                public void actionPerformed(@NotNull AnActionEvent event) {
                    InputEvent inputEvent = event.getInputEvent();
                    if (inputEvent instanceof MouseEvent) {
                        RelativePoint relativePoint = new RelativePoint((MouseEvent) inputEvent);
                        MapperStatementItem item = new MapperStatementItem((MouseEvent) inputEvent, info.getElement());
                        HotSwapNavigator hotSwapNavigator = new HotSwapNavigator();
                        MapperStatementRunnerNavigator msRunnerNavigator = new MapperStatementRunnerNavigator();
                        MapperNavigatorPopupStep navigatorsStep = new MapperNavigatorPopupStep(item, Arrays.asList(msRunnerNavigator, hotSwapNavigator));
                        // 展示菜单
                        ListPopup listPopup = JBPopupFactory.getInstance().createListPopup(navigatorsStep);
                        NavigationUtil.hidePopupIfDumbModeStarts(listPopup, info.getElement().getProject());
                        listPopup.show(relativePoint);
                    }
                }
            };
        }
        return rightAction;
    }
}
