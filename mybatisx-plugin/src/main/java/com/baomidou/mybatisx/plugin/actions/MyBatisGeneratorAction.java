package com.baomidou.mybatisx.plugin.actions;

import com.baomidou.mybatisx.feat.mybatis.generator.MyBatisGeneratorDialog;
import com.baomidou.mybatisx.feat.mybatis.generator.PsiTableInfo;
import com.baomidou.mybatisx.feat.mybatis.generator.TableInfo;
import com.baomidou.mybatisx.util.ArrayUtils;
import com.baomidou.mybatisx.util.MessageNotification;
import com.baomidou.mybatisx.util.PluginUtils;
import com.baomidou.mybatisx.util.PsiUtils;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Mybatis generator main action.
 */
public final class MyBatisGeneratorAction extends AnAction {

  public static boolean checkAssignableFrom(PsiElement element) {
    try {
      return DbTable.class.isAssignableFrom(element.getClass());
    } catch (Exception e) {
      return false;
    }
  }

  private static List<TableInfo> getChooseTables(AnActionEvent e) {
    PsiElement[] dbToolElements = PsiUtils.getPsiElementArray(e);
    if (dbToolElements == null || dbToolElements.length == 0) {
      return Collections.emptyList();
    }
    List<TableInfo> tablesToGenerate = new ArrayList<>();
    for (PsiElement element : dbToolElements) {
      if (element instanceof DbTable) {
        tablesToGenerate.add(new PsiTableInfo((DbTable) element));
      }
    }
    if (tablesToGenerate.isEmpty()) {
      return Collections.emptyList();
    }
    return tablesToGenerate;
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
    List<TableInfo> tablesToGenerate = getChooseTables(e);
    if (tablesToGenerate.isEmpty()) {
      MessageNotification.showMessageDialog("MyBatis Generator", "未选择表, 无法生成代码", null);
      return;
    }
    // 填充默认的选项
    MyBatisGeneratorDialog myBatisGeneratorDialog = new MyBatisGeneratorDialog(project);
    myBatisGeneratorDialog.fillData(project, tablesToGenerate);
    myBatisGeneratorDialog.show();
    myBatisGeneratorDialog.generateOnExist(tablesToGenerate);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
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
