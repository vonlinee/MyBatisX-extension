package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.feat.bean.TranslationAppComboBoxItem;
import com.baomidou.mybatisx.feat.ddl.TranslationAppEnum;
import com.baomidou.mybatisx.plugin.component.EnumComboBox;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.components.BorderLayoutPanel;
import lombok.Getter;
import lombok.Setter;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

import static com.baomidou.mybatisx.feat.ddl.TranslationAppEnum.BAIDU;
import static com.baomidou.mybatisx.feat.ddl.TranslationAppEnum.TENCENT;

@Getter
@Setter
public class TranslationSettingPanel {
  private JPanel rootPanel;
  private JPanel baiduAccountPanel;
  private JPanel translationBasePanel;
  private EnumComboBox<TranslationAppEnum> translationAppComboBox;
  private JTextField appIdText;
  private JRadioButton autoTranslationRadio;
  private JTextField secretText;
  private JPanel tencentAccountPanel;
  private JTextField secretId;
  private JTextField secretKey;

  private OtherSetting.State properties;

  public TranslationSettingPanel() {
    OtherSetting service = IntellijSDK.getService(OtherSetting.class);
    this.properties = service.myProperties;

    rootPanel = new JPanel(new VerticalLayout());
    SwingUtils.addTitleBorder(rootPanel, "基础配置");

    // 基础配置面板
    HBox hBox = new HBox();
    GridBag gb = UIHelper.newGridBagLayoutConstraints();
    hBox.add(new Label("自动翻译 :"));
    autoTranslationRadio = new JRadioButton();
    hBox.add(autoTranslationRadio);
    hBox.add(new Label("翻译组件 :"));
    hBox.add(translationAppComboBox = new EnumComboBox<>(), gb.next());

    rootPanel.add(hBox, BorderLayout.NORTH);

    translationBasePanel = new JPanel(new CardLayout());
    // 翻译切换面板容器
    BorderLayoutPanel jPanel = new BorderLayoutPanel();
    jPanel.addToCenter(translationBasePanel);
    rootPanel.add(jPanel, BorderLayout.SOUTH);

    // 不同的翻译实现面板

    // 腾讯翻译
    GridBag gb1 = UIHelper.newGridBagLayoutConstraints();
    tencentAccountPanel = new JPanel(new GridBagLayout());
    tencentAccountPanel.add(new Label("SecretId :"), gb1.nextLine().next());
    secretId = new JTextField();
    tencentAccountPanel.add(secretId, gb1.next());
    tencentAccountPanel.add(new Label("SecretKey :"), gb1.nextLine().next());
    secretKey = new JTextField();
    tencentAccountPanel.add(secretKey, gb1.next());

    SwingUtils.addTitleBorder(tencentAccountPanel, "SecretId & SecretKey");

    // 百度翻译
    gb1.reset();
    baiduAccountPanel = new JPanel(new GridBagLayout());
    baiduAccountPanel.add(new Label("appId :"), gb1.nextLine().next());
    appIdText = new JTextField();
    baiduAccountPanel.add(appIdText, gb1.next());
    baiduAccountPanel.add(new Label("SecretText :"), gb1.nextLine().next());
    secretText = new JTextField();
    baiduAccountPanel.add(secretText, gb1.next());
    SwingUtils.addTitleBorder(baiduAccountPanel, "appId & SecretText");

    translationBasePanel.add(tencentAccountPanel);
    translationBasePanel.add(baiduAccountPanel);

    baiduAccountPanel.setVisible(StringUtils.equals(BAIDU.getValue(), properties.getTranslationAppComboBox()));
    tencentAccountPanel.setVisible(StringUtils.equals(TENCENT.getValue(), properties.getTranslationAppComboBox()));
    /*翻译组件下拉框初始化*/
    translationAppComboBoxInit();
    /*自动翻译单元框*/
    autoTranslationRadioInit();

    appIdText.setText(properties.getAppIdText());
    secretText.setText(properties.getSecretText());

    secretId.setText(properties.getSecretId());
    secretKey.setText(properties.getSecretKey());
  }

  private void autoTranslationRadioInit() {
    // 从配置中设置值
    autoTranslationRadio.setSelected(properties.getAutoTranslationRadio());
    // 单选按钮设置事件
    autoTranslationRadio.addActionListener(e -> {
      if (autoTranslationRadio.isSelected()) {
        TranslationAppComboBoxItem item = (TranslationAppComboBoxItem) translationAppComboBox.getSelectedItem();
        if (item != null) {
          if (translationAppComboBox.getItemCount() > 2) {
            translationAppComboBox.setSelectedIndex(1);
          }
          baiduAccountPanel.setVisible(StringUtils.equals(BAIDU.getValue(), item.getValue()));
          tencentAccountPanel.setVisible(StringUtils.equals(TENCENT.getValue(), item.getValue()));
        }
        if (!translationBasePanel.isVisible()) {
          translationBasePanel.setVisible(true);
        }
      } else {
        baiduAccountPanel.setVisible(false);
        tencentAccountPanel.setVisible(false);
        translationBasePanel.setVisible(false);
        translationAppComboBox.setSelectedIndex(0);
      }
    });
  }

  private void translationAppComboBoxInit() {
    // 填充下拉框数据
    for (TranslationAppEnum e : TranslationAppEnum.values()) {
      translationAppComboBox.addItems(new TranslationAppComboBoxItem(e.getName(), e.getValue()));
    }
    // 从配置中设置值
    TranslationAppEnum appEnum = TranslationAppEnum.findByValue(properties.getTranslationAppComboBox());
    translationAppComboBox.setSelectedItem(new TranslationAppComboBoxItem(appEnum.getName(), appEnum.getValue()));
    translationAppComboBox.addItemListener(e -> {
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
      if (!translationBasePanel.isVisible()) {
        translationBasePanel.setVisible(true);
      }
    });
  }
}
