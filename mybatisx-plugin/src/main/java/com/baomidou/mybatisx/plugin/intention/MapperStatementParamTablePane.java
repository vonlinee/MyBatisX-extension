package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;

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

  public MapperStatementParamTablePane(AnActionButton[] actions) {
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
    table.setAll(paramNodeList);
  }

  public void resetAll(List<ParamNode> paramNodeList) {
    table.resetAll(paramNodeList);
  }
}
