package com.baomidou.plugin.idea.mybatisx.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;

/**
 * 消息通知工具类
 */
public final class Notifications {

    private Notifications() {
    }

    public static void notify(String content, NotificationType type) {
        NotificationGroupManager groupManager = NotificationGroupManager.getInstance();
        NotificationGroup notificationGroup = groupManager.getNotificationGroup("notifyAction");
        Notification notification = notificationGroup.createNotification("[MyBatisX] " + content, type);
        com.intellij.notification.Notifications.Bus.notify(notification);
    }

    public static void error(String msg) {
        notify(msg, NotificationType.ERROR);
    }

    public static void warning(String msg) {
        notify(msg, NotificationType.WARNING);
    }

    public static void info(String msg) {
        notify(msg, NotificationType.INFORMATION);
    }
}
