package com.baomidou.plugin.idea.mybatisx.setting.ui;

import com.baomidou.plugin.idea.mybatisx.ddl.TranslationAppEnum;
import com.baomidou.plugin.idea.mybatisx.model.ComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.model.TranslationAppComboBoxItem;
import com.baomidou.plugin.idea.mybatisx.setting.JavaBean2DDLSetting;
import com.intellij.openapi.components.ServiceManager;
import lombok.Getter;
import lombok.Setter;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.baomidou.plugin.idea.mybatisx.ddl.TranslationAppEnum.BAIDU;
import static com.baomidou.plugin.idea.mybatisx.ddl.TranslationAppEnum.EMPTY;
import static com.baomidou.plugin.idea.mybatisx.ddl.TranslationAppEnum.TENCENT;

@Getter
@Setter
public class TranslationSettingPanel {
    private JPanel mainPanel;
    private JPanel baiduAccountPanel;
    private JPanel translationSettingPanel;
    private JPanel translationBasePanel;
    private JComboBox<ComboBoxItem> translationAppComboBox;
    private JTextField appIdText;
    private JRadioButton autoTranslationRadio;
    private JTextField secretText;
    private JPanel tencentAccountPanel;
    private JTextField secretId;
    private JTextField secretKey;

    private JavaBean2DDLSetting.MySettingProperties properties;

    public TranslationSettingPanel() {
        JavaBean2DDLSetting service = ServiceManager.getService(JavaBean2DDLSetting.class);
        this.properties = service.myProperties;

        accountPanelInit();
        /*翻译组件下拉框初始化*/
        translationAppComboBoxInit();
        /*自动翻译单元框*/
        autoTranslationRadioInit();

        appIdText.setText(properties.getAppIdText());
        secretText.setText(properties.getSecretText());

        secretId.setText(properties.getSecretId());
        secretKey.setText(properties.getSecretKey());
    }

    private void accountPanelInit() {
        baiduAccountPanel.setVisible(StringUtils.equals(BAIDU.getValue(), properties.getTranslationAppComboBox()));
        tencentAccountPanel.setVisible(StringUtils.equals(TENCENT.getValue(), properties.getTranslationAppComboBox()));
    }

    private void autoTranslationRadioInit() {
        // 从配置中设置值
        autoTranslationRadio.setSelected(properties.getAutoTranslationRadio());
        // 单选按钮设置事件
        autoTranslationRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autoTranslationRadio.isSelected()) {
                    TranslationAppComboBoxItem item = (TranslationAppComboBoxItem) translationAppComboBox.getSelectedItem();
                    assert item != null;
                    baiduAccountPanel.setVisible(StringUtils.equals(BAIDU.getValue(), item.getValue()));
                    tencentAccountPanel.setVisible(StringUtils.equals(TENCENT.getValue(), item.getValue()));
                    return;
                }
                baiduAccountPanel.setVisible(false);
                tencentAccountPanel.setVisible(false);
            }
        });
    }

    private void translationAppComboBoxInit() {
        // 填充下拉框数据
        translationAppComboBox.addItem(new TranslationAppComboBoxItem(EMPTY));
        translationAppComboBox.addItem(new TranslationAppComboBoxItem(TENCENT));
        translationAppComboBox.addItem(new TranslationAppComboBoxItem(BAIDU));
        // 从配置中设置值
        translationAppComboBox.setSelectedItem(
            new TranslationAppComboBoxItem(
                TranslationAppEnum.findByValue(properties.getTranslationAppComboBox())
            )
        );
        translationAppComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 设置下拉框被选中的事件
                    TranslationAppComboBoxItem item = (TranslationAppComboBoxItem) e.getItem();
                    if (StringUtils.equals(TENCENT.getValue(), item.getValue())) {
                        autoTranslationRadio.setSelected(true);
                        tencentAccountPanel.setVisible(true);
                        baiduAccountPanel.setVisible(false);
                        return;
                    }
                    if (StringUtils.equals(BAIDU.getValue(), item.getValue())) {
                        autoTranslationRadio.setSelected(true);
                        tencentAccountPanel.setVisible(false);
                        baiduAccountPanel.setVisible(true);
                        return;
                    }
                    autoTranslationRadio.setSelected(false);
                    tencentAccountPanel.setVisible(false);
                    baiduAccountPanel.setVisible(false);
                }
            }
        });
    }
}
