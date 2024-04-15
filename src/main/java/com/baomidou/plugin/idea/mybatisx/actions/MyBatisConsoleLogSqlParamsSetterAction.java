package com.baomidou.plugin.idea.mybatisx.actions;

import com.baomidou.plugin.idea.mybatisx.util.SqlUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * 从 mybatis 控制台 sql 日志中提取实际 sql
 */
public class MyBatisConsoleLogSqlParamsSetterAction extends AnAction {

    private static final NotificationGroup NOTIFICATION_GROUP =
        new NotificationGroup("SqlParamsSetter.NotificationGroup", NotificationDisplayType.BALLOON, true);

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Project project = editor.getProject();
        if (project == null) {
            return;
        }

        SelectionModel model = editor.getSelectionModel();
        final String selectedMybatisLogs = model.getSelectedText();
        if (StringUtils.isBlank(selectedMybatisLogs)) {
            return;
        }

        String sql;
        try {
            sql = SqlUtils.parseExecutableSqlFromMyBatisLog(selectedMybatisLogs);
        } catch (Exception ex) {
            notify(project, String.format("Failed at: %s", ex), NotificationType.ERROR);
            return;
        }

        if (StringUtils.isBlank(sql)) {
            notify(project, "Selected area should contain both [Preparing:] in the 1st line and [Parameters:] in the 2nd line.", NotificationType.WARNING);
            return;
        }

        StringSelection selection = new StringSelection(sql);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        notify(project, "Success, copied to clipboard.", NotificationType.INFORMATION);
    }

    private void notify(Project project, String message, NotificationType type) {
        Notification success = NOTIFICATION_GROUP.createNotification(message, type);
        Notifications.Bus.notify(success, project);
    }
}
