package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.plugin.components.TreeModel;
import com.baomidou.mybatisx.plugin.components.TreeView;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.EditableTreeModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Collection;

/**
 * 模板树形结构
 */
public class TemplateTreeView extends TreeView<TemplateInfo> {

  TemplateTreeViewModel model;

  public TemplateTreeView() {
    super();
    setModel(model = new TemplateTreeViewModel(this));
    setCellRenderer(new DefaultTreeCellRenderer() {
      @Override
      public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                    boolean sel,
                                                    boolean expanded,
                                                    boolean leaf,
                                                    int row,
                                                    boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        // 检查节点是否为复杂对象
        if (value instanceof DefaultMutableTreeNode) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
          Object userObject = node.getUserObject();
          if (userObject instanceof TemplateInfo) {
            TemplateInfo templateInfo = (TemplateInfo) userObject;
            // 自定义显示内容，例如显示姓名和年龄
            setText(templateInfo.getName());
          }
        }
        return this;
      }
    });
  }

  @Override
  protected AnActionButtonRunnable getAddAction() {
    return anActionButton -> {
    };
  }

  private static class TemplateTreeViewModel extends TreeModel<TemplateInfo> implements EditableTreeModel {

    public TemplateTreeViewModel(TreeView<TemplateInfo> treeView) {
      super(treeView);
    }

    /**
     * @param parentOrNeighbour selected node, maybe used as parent or as a neighbour
     * @return 新创建的元素的路径
     */
    @Override
    public TreePath addNode(TreePath parentOrNeighbour) {
      if (parentOrNeighbour.getParentPath() == null) {
        return new TreePath(new Object[]{getRoot(), new DefaultMutableTreeNode("New Template")});
      }
      return parentOrNeighbour.pathByAddingChild(new DefaultMutableTreeNode("New Template"));
    }

    @Override
    public void removeNode(TreePath path) {

    }

    @Override
    public void removeNodes(Collection<? extends TreePath> path) {

    }

    @Override
    public void moveNodeTo(TreePath parentOrNeighbour) {

    }
  }
}
