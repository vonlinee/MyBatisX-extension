package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.feat.generate.ClassGenerateDialogWrapper;
import com.baomidou.mybatisx.feat.generate.dto.GenerateConfig;
import com.baomidou.mybatisx.feat.generate.dto.TableUIInfo;
import com.baomidou.mybatisx.feat.generate.dto.TemplateContext;
import com.baomidou.mybatisx.plugin.setting.TemplatesSettings;
import com.baomidou.mybatisx.feat.generate.template.CodeGenerator;
import com.baomidou.mybatisx.util.ArrayUtils;
import com.baomidou.mybatisx.util.PluginUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.intellij.database.model.DasObject;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Mybatis generator main action.
 */
public final class MyBatisGeneratorAction extends AnAction {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisGeneratorAction.class);

    public static boolean checkAssignableFrom(PsiElement element) {
        try {
            return DbTable.class.isAssignableFrom(element.getClass());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 代码生成
     * 点击后打开插件主页面
     *
     * @param e AnActionEvent
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        PsiElement[] dbToolElements = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (dbToolElements == null || dbToolElements.length == 0) {
            logger.error("未选择表, 无法生成代码");
            return;
        }
        ClassGenerateDialogWrapper classGenerateDialogWrapper = new ClassGenerateDialogWrapper(project);
        List<DbTable> tablesToGenerate = new ArrayList<>();
        for (PsiElement element : dbToolElements) {
            if (element instanceof DbTable) {
                tablesToGenerate.add((DbTable) element);
            }
        }
        if (tablesToGenerate.isEmpty()) {
            return;
        }
        // 填充默认的选项
        classGenerateDialogWrapper.fillData(project, tablesToGenerate);
        classGenerateDialogWrapper.show();
        // 模态窗口选择 OK, 生成相关代码
        if (classGenerateDialogWrapper.getExitCode() == Messages.YES) {
            // 生成代码
            GenerateConfig generateConfig = classGenerateDialogWrapper.determineGenerateConfig();
            if (!generateConfig.checkGenerate()) {
                return;
            }
            generateCode(project, tablesToGenerate, generateConfig);
        }
    }

    public void generateCode(Project project, List<DbTable> psiElements, GenerateConfig generateConfig) {
        try {
            // 保存配置, 更新最后一次存储的配置
            TemplatesSettings templatesSettings = TemplatesSettings.getInstance(project);
            TemplateContext templateConfigs = templatesSettings.getTemplateContext();
            templateConfigs.setGenerateConfig(generateConfig);
            templateConfigs.setTemplateName(generateConfig.getTemplatesName());
            templateConfigs.setModuleName(generateConfig.getModuleName());
            templatesSettings.setTemplateContext(templateConfigs);

            Map<String, DbTable> tableMapping = psiElements.stream()
                    .collect(Collectors.toMap(DasObject::getName, a -> a, (a, b) -> a));
            for (TableUIInfo uiInfo : generateConfig.getTableUIInfoList()) {
                String tableName = uiInfo.getTableName();
                DbTable dbTable = tableMapping.get(tableName);
                if (dbTable != null) {
                    // 生成代码
                    CodeGenerator.generate(project,
                            generateConfig,
                            templatesSettings.getTemplateSettingMap(),
                            dbTable,
                            uiInfo.getClassName(),
                            uiInfo.getTableName());
                }
            }
            VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
            logger.info("全部代码生成成功, 文件内容已更新. config: {}", generateConfig);
        } catch (Exception e) {
            logger.error("生成代码出错", e);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        boolean visible = true;
        PsiElement[] psiElements = PsiUtils.getPsiElements(e);
        if (ArrayUtils.isEmpty(psiElements)) {
            visible = false;
        } else {
            boolean existsDbTools = PluginUtils.existsDbTools();
            if (!existsDbTools) {
                visible = false;
            }
            for (PsiElement psiElement : psiElements) {
                if (checkAssignableFrom(psiElement)) {
                    visible = true;
                    break;
                }
            }
        }
        // 未安装Database Tools插件时，不展示菜单
        e.getPresentation().setEnabledAndVisible(visible);
    }
}
