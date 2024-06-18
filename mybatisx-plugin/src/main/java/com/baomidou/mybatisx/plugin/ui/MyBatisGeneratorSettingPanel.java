package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.plugin.setting.OtherSetting;
import com.baomidou.mybatisx.util.IntellijSDK;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class MyBatisGeneratorSettingPanel {
    private JPanel rootPanel;
    private JTextField tableText;
    private JTextField idText;
    private JTextField commendText;
    private JTextField tablePropertyText;

    private OtherSetting.State properties;

    public MyBatisGeneratorSettingPanel() {
        this.properties = IntellijSDK.getService(OtherSetting.class).getProperties();
    }
}
