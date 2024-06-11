package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.plugin.setting.JavaBean2DDLSetting;
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

    private JavaBean2DDLSetting.MySettingProperties properties;

    public MyBatisGeneratorSettingPanel() {
        this.properties = IntellijSDK.getService(JavaBean2DDLSetting.class).getProperties();
    }
}
