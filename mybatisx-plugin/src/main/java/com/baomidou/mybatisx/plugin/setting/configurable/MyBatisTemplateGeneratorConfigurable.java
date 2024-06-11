package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.feat.generate.setting.TemplatesSettings;
import com.baomidou.mybatisx.plugin.ui.MyBatisXTemplateSettings;
import com.intellij.openapi.options.ConfigurableBase;
import com.intellij.openapi.options.ConfigurableUi;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Mybatisx 模板生成代码配置
 */
public class MyBatisTemplateGeneratorConfigurable extends ConfigurableBase<MyBatisTemplateGeneratorConfigurable.MyConfigurableUi, MyBatisXTemplateSettings> {

    /**
     * 项目
     */
    private final Project project;
    private final MyBatisXTemplateSettings mybatisXTemplateSettings = new MyBatisXTemplateSettings();

    protected MyBatisTemplateGeneratorConfigurable(Project project) {
        super("mybatisx.template", "MybatisX Template", "mybatisx.template");
        this.project = project;
    }

    @NotNull
    @Override
    protected MyBatisXTemplateSettings getSettings() {
        return mybatisXTemplateSettings;
    }

    @Override
    protected MyConfigurableUi createUi() {
        // 如果是社区版本, 就不需要配置代码生成器
        TemplatesSettings instance = TemplatesSettings.getInstance(project);
        mybatisXTemplateSettings.loadBySettings(instance);
        return new MyConfigurableUi(instance);
    }

    public class MyConfigurableUi implements ConfigurableUi<MyBatisXTemplateSettings> {

        private final TemplatesSettings templatesSettings;

        public MyConfigurableUi(TemplatesSettings templatesSettings) {
            this.templatesSettings = templatesSettings;
        }

        @Override
        public void reset(@NotNull MyBatisXTemplateSettings settings) {

        }

        @Override
        public boolean isModified(@NotNull MyBatisXTemplateSettings settings) {
            return settings.isModified();
        }

        @Override
        public void apply(@NotNull MyBatisXTemplateSettings settings) throws ConfigurationException {
            // 只替换当前选中的内容?
            settings.apply(templatesSettings);
        }

        @Override
        public @NotNull JComponent getComponent() {
            return mybatisXTemplateSettings.getRootPanel();
        }
    }
}
