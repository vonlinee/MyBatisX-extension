package com.baomidou.mybatisx.plugin.actions

import com.baomidou.mybatisx.plugin.ui.dialog.BeanToolDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * JavaBean 工具
 */
class BeanToolAction : AnAction() {
    var dialog: BeanToolDialog? = null

    @Override
    override fun actionPerformed(event: AnActionEvent) {
        if (dialog == null) {
            dialog = BeanToolDialog(event.project)
        }
        dialog!!.show()
    }
}
