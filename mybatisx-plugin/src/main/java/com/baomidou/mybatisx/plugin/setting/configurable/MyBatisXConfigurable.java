package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.setting.MyBatisXSettings;
import com.baomidou.mybatisx.plugin.ui.MyBatisSettingForm;
import com.baomidou.mybatisx.util.MapperIcon;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import org.mybatisx.extension.agent.server.AgentServer;

import javax.swing.*;

/**
 * The type Mybatis configurable.
 *
 * @author yanglin
 */
public class MyBatisXConfigurable implements SearchableConfigurable {

    private final MyBatisXSettings mybatisXSettings;

    private MyBatisSettingForm mybatisSettingForm;

    /**
     * Instantiates a new Mybatis configurable.
     */
    public MyBatisXConfigurable() {
        mybatisXSettings = MyBatisXSettings.getInstance();
    }

    @Override
    public String getId() {
        return "MyBatisX";
    }

    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return getId();
    }

    /**
     * 配置界面显示的根组件
     *
     * @return 根组件
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if (null == mybatisSettingForm) {
            this.mybatisSettingForm = new MyBatisSettingForm();
        }
        return mybatisSettingForm.root;
    }

    @Override
    public boolean isModified() {
        return !mybatisXSettings.getInsertGenerator().equals(mybatisSettingForm.insertPatternTextField.getText())
               || !mybatisXSettings.getDeleteGenerator().equals(mybatisSettingForm.deletePatternTextField.getText())
               || !mybatisXSettings.getUpdateGenerator().equals(mybatisSettingForm.updatePatternTextField.getText())
               || !mybatisXSettings.getSelectGenerator().equals(mybatisSettingForm.selectPatternTextField.getText())
               || (mybatisSettingForm.defaultRadioButton.isSelected() ?
            MapperIcon.BIRD.name().equals(mybatisXSettings.getMapperIcon())
            : MapperIcon.DEFAULT.name().equals(mybatisXSettings.getMapperIcon()))
               || mybatisXSettings.getState().hotswapEnabled != mybatisSettingForm.hotSwapCheckBox.isSelected();
    }

    @Override
    public void apply() {
        mybatisXSettings.setInsertGenerator(mybatisSettingForm.insertPatternTextField.getText());
        mybatisXSettings.setDeleteGenerator(mybatisSettingForm.deletePatternTextField.getText());
        mybatisXSettings.setUpdateGenerator(mybatisSettingForm.updatePatternTextField.getText());
        mybatisXSettings.setSelectGenerator(mybatisSettingForm.selectPatternTextField.getText());

        MapperIcon mapperIcon = mybatisSettingForm.defaultRadioButton.isSelected() ? MapperIcon.DEFAULT : MapperIcon.BIRD;
        mybatisXSettings.setMapperIcon(mapperIcon.name());

        MyBatisXSettings.State state = mybatisXSettings.getState();
        state.hotswapEnabled = mybatisSettingForm.hotSwapCheckBox.isSelected();

        if (!state.hotswapEnabled) {

        }
    }

    @Override
    public void reset() {
        MyBatisXSettings.State state = mybatisXSettings.getState();
        mybatisSettingForm.insertPatternTextField.setText(mybatisXSettings.getInsertGenerator());
        mybatisSettingForm.deletePatternTextField.setText(mybatisXSettings.getDeleteGenerator());
        mybatisSettingForm.updatePatternTextField.setText(mybatisXSettings.getUpdateGenerator());
        mybatisSettingForm.selectPatternTextField.setText(mybatisXSettings.getSelectGenerator());
        mybatisSettingForm.hotSwapCheckBox.setSelected(state.hotswapEnabled);
        JRadioButton jRadioButton = mybatisSettingForm.birdRadioButton;
        if (MapperIcon.DEFAULT.name().equals(mybatisXSettings.getMapperIcon())) {
            jRadioButton = mybatisSettingForm.defaultRadioButton;
        }
        jRadioButton.setSelected(true);
    }

    @Override
    public void disposeUIResources() {
        mybatisSettingForm.root = null;
    }
}
