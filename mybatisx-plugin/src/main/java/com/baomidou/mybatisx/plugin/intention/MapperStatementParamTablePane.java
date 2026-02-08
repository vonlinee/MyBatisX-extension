package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.ui.UIHelper;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.JsonUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.List;
import java.util.Map;

/**
 * Mapper 语句参数表格面板
 */
public class MapperStatementParamTablePane extends JScrollPane {

  private final MSParamTreeTable table;

  public MapperStatementParamTablePane(AnAction[] actions) {
    table = new MSParamTreeTable();
    // 添加工具栏
    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(table);
    decorator.setAddAction(aab -> {
      ParamNode child = new ParamNode("param", "", ParamDataType.STRING);
      final TreeTableTree tree = table.getTree();
      TreePath selectionPath = table.getTree().getSelectionPath();
      ListTreeTableModelOnColumns model = (ListTreeTableModelOnColumns) table.getTreeTableModel();
      if (selectionPath == null) {
        ParamNode root = (ParamNode) tree.getModel().getRoot();
        model.insertNodeInto(child, root, root.getChildCount());
        root.add(child);
        tree.scrollPathToVisible(new TreePath(child.getPath()));
      } else {
        ParamNode parent = (ParamNode) selectionPath.getLastPathComponent();
        model.insertNodeInto(child, parent, parent.getChildCount());
        parent.add(child);
        tree.scrollPathToVisible(new TreePath(child.getPath()));
      }
    });
    decorator.setRemoveAction(anActionButton -> {
      TreeNode removedNode = table.deleteSelectedRow();
      if (!removedNode.isLeaf()) {
        SwingUtils.expandAll(table.getTree());
      }
    });
    decorator.addExtraActions(actions);

    decorator.addExtraAction(new AnAction(() -> "Export Params As Json", PlatformIcons.EXPORT_ICON) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        Map<String, Object> map = CollectionUtils.expandKeys(getParamsAsMap(), StringUtils.SPLITTER);
        String string = JsonUtils.toJsonPrettyString(map);
        ParamExportResultDialog dialog = new ParamExportResultDialog(e.getProject(), string);
        dialog.show();
      }
    });

    setViewportView(decorator.createPanel());
  }

  /**
   * 将参数名转化为嵌套Map形式
   *
   * @return 扁平化Map
   */
  public Map<String, Object> getParamsAsMap() {
    return table.getParamsAsMap();
  }

  public void setAll(List<ParamNode> paramNodeList) {
    IntellijSDK.invokeLater(() -> table.setAll(paramNodeList));
  }

  public void resetAll(List<ParamNode> paramNodeList) {
    table.resetAll(paramNodeList);
  }

  /**
   * 参数导出JSON弹窗
   */
  private static class ParamExportResultDialog extends DialogWrapper {

    private final Project project;
    private final String initialValue;
    private LanguageTextField textField;

    protected ParamExportResultDialog(@Nullable Project project, @Nullable String initialValue) {
      super(project);
      this.project = project;
      this.initialValue = initialValue == null ? "" : initialValue;
      setModal(true);
      this.setSize(600, 400);
      this.setTitle("Parameters");
      setOKButtonText("Copy To Clipboard");
      init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
      textField = new LanguageTextField(JsonLanguage.INSTANCE, project, initialValue);
      UIHelper.setEmptyBorder(textField);
      return textField;
    }

    @Override
    protected void doOKAction() {
      super.doOKAction();
      String text = textField.getText();
      if (text.isBlank()) {
        return;
      }
      SwingUtils.copyToClipboard(text);
    }
  }
}
