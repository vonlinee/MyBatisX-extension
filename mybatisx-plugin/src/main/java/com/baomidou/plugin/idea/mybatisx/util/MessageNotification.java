package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * 消息通知
 */
public abstract class MessageNotification {

    private static final NotificationGroup NOTIFICATION_GROUP =
        new NotificationGroup(PluginUtils.PLUGIN_NAME + ".NotificationGroup", NotificationDisplayType.BALLOON, true);

    public static void notifyError(Project project, String title, String message) {
        // 创建一个Notification对象，设置标题、内容、通知类型等
        Notification notification = new Notification(
            NOTIFICATION_GROUP.getDisplayId(), // 使用之前创建的NotificationGroup
            title, // 通知的标题
            message, // 通知的内容
            NotificationType.ERROR, // 通知的类型（错误）
            null // 附加的操作按钮，如果需要的话
        );
        // 显示通知，这里我们设置为显示在事件日志中，并且允许气球通知（如果需要的话）
        Notifications.Bus.notify(notification, project);
    }

    public static void showErrorDialog(String title, String message) {
        Messages.showErrorDialog(message, title);
    }
}
