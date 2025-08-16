package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.plugin.components.CodeArea;
import com.baomidou.mybatisx.plugin.components.SplitPane;
import com.baomidou.mybatisx.plugin.components.TabPane;
import com.baomidou.mybatisx.plugin.components.TreeModel;
import com.baomidou.mybatisx.plugin.components.TreeView;
import com.baomidou.mybatisx.plugin.components.VBox;
import com.baomidou.mybatisx.plugin.setting.TemplatesSettings;
import com.baomidou.mybatisx.util.FileUtils;
import com.intellij.ide.fileTemplates.impl.FileTemplateConfigurable;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

/**
 * 模板设置面板
 * File and Code Template
 *
 * @see FileTemplateConfigurable
 * @see TemplatesSettings
 */
public class TemplateSettingPane extends TabPane {

  TemplateTreeView ttv = new TemplateTreeView();
  CodeArea currentTemplateContentText = new CodeArea();

  public TemplateSettingPane() {
    SplitPane splitPane = new SplitPane(false, 0.4f);

    splitPane.setFirstComponent(ttv.createPanel());
    ttv.setMinimumSize(new Dimension(400, ttv.getHeight()));

    VBox vBox = new VBox();
    splitPane.setSecondComponent(vBox);

    currentTemplateContentText.setPreferredHeight(400);
    vBox.add(currentTemplateContentText);

    addTab("代码生成", splitPane);

    ttv.addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent e) {
        TreeView<TemplateInfo> treeView = TreeView.getTreeView(e);
        TemplateInfo templateInfo = treeView.getSelectedItem();
        currentTemplateContentText.setText(FileUtils.readStringUTF8(templateInfo.getPath()));
      }
    });

    TemplatesSettings templatesSettings = TemplatesSettings.getInstance();
    TreeModel<TemplateInfo> treeModel = ttv.getTreeModel();
    for (TemplateInfo template : templatesSettings.getTemplates()) {
      treeModel.addChild(template);
    }
    ttv.setRootVisible(false);
  }

  /**
   * 配置是否修改过
   *
   * @return
   */
  public boolean hasChanged() {
    return currentTemplateContentText.isTextLengthChanged();
  }
}
