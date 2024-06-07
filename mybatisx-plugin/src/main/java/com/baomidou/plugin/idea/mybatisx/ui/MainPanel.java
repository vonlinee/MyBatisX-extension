package com.baomidou.plugin.idea.mybatisx.ui;

import com.baomidou.plugin.idea.mybatisx.model.Field;
import com.baomidou.plugin.idea.mybatisx.service.MainService;
import com.baomidou.plugin.idea.mybatisx.util.DdlFormatUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.psi.PsiClass;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.List;

@Getter
@Setter
public class MainPanel {
    private JPanel content;
    private JButton copy;
    private JRadioButton removeSuperPropertyRadio;
    private JTextArea sqlContentPanel;
    private JLabel jlabel;
    private String contentTxt;

    private MainService mainService;
    private PsiClass psiClass;

    public MainPanel() {
        copy.setText("一键复制");
        jlabel.setText("去除父类属性:");
        sqlContentPanelInit(contentTxt);
        // 复制按钮
        copyButtonInit(sqlContentPanel);
        // 移除父类属性单选按钮
        removeSuperPropertyRadioInit(mainService, psiClass);
    }

    private void sqlContentPanelInit(String contentTxt) {
        // 设置文本
        sqlContentPanel.setText(contentTxt);
    }

    /**
     * 移除父类属性单选按钮初始化
     */
    private void removeSuperPropertyRadioInit(MainService mainService, PsiClass currentClass) {
        removeSuperPropertyRadio.addActionListener(e -> {
            String tableName = mainService.getTableName(currentClass);
            List<Field> fieldList = getFieldListByRadioSelect(mainService, currentClass);
            String script = DdlFormatUtil.buildDdlScript(tableName, fieldList);
            sqlContentPanel.setText(script);
        });
    }

    private List<Field> getFieldListByRadioSelect(MainService mainService, PsiClass currentClass) {
        if (removeSuperPropertyRadio.isSelected()) {
            /*获取当前类所有字段*/
            return mainService.getFieldList(currentClass, false);
        }
        return mainService.getFieldList(currentClass);
    }

    /**
     * 复制安妮初始化
     *
     * @param contentTxt 操作的内容文本域
     */
    private void copyButtonInit(JTextArea contentTxt) {
        copy.addActionListener(e -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            // 封装文本内容
            Transferable trans = new StringSelection(contentTxt.getText());
            // 把文本内容设置到系统剪贴板
            clipboard.setContents(trans, null);

            Notification notification = new Notification("System Clipboard",
                "Notify success", "复制成功!", NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);
        });
    }
}
