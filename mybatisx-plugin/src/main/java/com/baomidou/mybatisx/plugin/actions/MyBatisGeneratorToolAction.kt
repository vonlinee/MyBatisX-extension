package com.baomidou.mybatisx.plugin.actions

import com.baomidou.mybatisx.feat.mybatis.generator.MyBatisGeneratorDialog
import com.baomidou.mybatisx.feat.mybatis.generator.TableInfo
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MyBatisGeneratorToolAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project
    // 填充默认的选项
    val dialog = MyBatisGeneratorDialog(project)
    dialog.fillData(project, listOf<TableInfo>())
    dialog.show()
  }

  override fun getActionUpdateThread(): ActionUpdateThread {
    return ActionUpdateThread.BGT
  }
}
