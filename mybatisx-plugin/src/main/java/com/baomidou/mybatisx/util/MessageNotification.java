package com.baomidou.mybatisx.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 消息通知
 */
public abstract class MessageNotification {

  public static final String GROUP_ID_SYSTEM_CLIPBOARD = "System Clipboard";

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

  public static void info(String groupId, String title, String content) {
    Notification notification = new Notification(groupId,
      title, content, NotificationType.INFORMATION);
    Notifications.Bus.notify(notification);
  }

  public static void showErrorDialog(String title, String message) {
    Messages.showErrorDialog(message, title);
  }

  public static void showErrorDialog(String message) {
    Messages.showErrorDialog(message, "ERROR");
  }

  public static void showMessageDialog(String message,
                                       @NotNull String title,
                                       @Nullable Icon icon) {
    Messages.showMessageDialog(message, title, icon);
  }
}
