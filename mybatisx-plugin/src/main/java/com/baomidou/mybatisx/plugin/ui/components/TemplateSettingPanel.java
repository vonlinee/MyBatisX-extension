package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.TabPanel;
import com.baomidou.mybatisx.plugin.setting.GlobalTemplateSettings;

/**
 * 模板设置面板
 */
public class TemplateSettingPanel extends TabPanel {

    public TemplateSettingPanel() {
        TemplateTableView tablePanel = new TemplateTableView();
        tablePanel.addRows(GlobalTemplateSettings.getInstance().getTemplates());

        addTab("代码生成", tablePanel.createPanel());
    }
}
