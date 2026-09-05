package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.treeStructure.Tree;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 * 默认不展示根节点
 *
 * @param <T>
 */
public class TreeView<T> extends ScrollPane {

  private DefaultMutableTreeNode root;

  @Getter
  protected final Tree tree;

  public TreeView() {
    super(new Tree());
    this.tree = (Tree) getViewport().getView();
    root = (DefaultMutableTreeNode) tree.getModel().getRoot();

    tree.setRootVisible(false);
    tree.setShowsRootHandles(false); // 隐藏根节点的展开手柄
  }

  protected void setModel(TreeModel<T> treeModel) {
    tree.setModel(treeModel);
    root = (DefaultMutableTreeNode) treeModel.getRoot();
    tree.setRootVisible(false);
    tree.setShowsRootHandles(false);
  }

  @SuppressWarnings("unchecked")
  public static <T> TreeView<T> getTreeView(TreeSelectionEvent event) {
    Object source = event.getSource();
    if (source instanceof TreeView) {
      return (TreeView<T>) source;
    }
    throw new UnsupportedOperationException("event source is not a TreeView.");
  }

  public final void addChild(DefaultMutableTreeNode newChild) {
    root.add(newChild);
  }

  public final void addChild(T item) {
    this.getTreeModel().addChild(item);
  }

  public void setRootVisible(boolean rootVisible) {
    if (rootVisible) {
      tree.setRootVisible(true);
    } else {
      tree.setRootVisible(false);
      if (getRootNode().getChildCount() > 0) {
        tree.setRootVisible(true);
        // 设置根节点展开, 需要有子节点才有效果
        tree.expandRow(0);
        // 隐藏根节点
        tree.setRootVisible(false);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public final TreeModel<T> getTreeModel() {
    return (TreeModel<T>) tree.getModel();
  }

  /**
   * 要等到根节点下面有节点后才能设置setRootVisible(false)
   */
  public final void expandRoot() {
    tree.setRootVisible(true);
    tree.expandPath(new TreePath(getRootNode()));
    tree.setRootVisible(false);
  }

  public final void expandAll() {
    setRootVisible(true);
    expandRoot();
    tree.setShowsRootHandles(false);
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }
    setRootVisible(false);
  }

  public final void collapseAll() {
    setRootVisible(true);
    tree.expandPath(new TreePath(getRootNode()));
    for (int i = tree.getRowCount() - 1; i > 0; i--) {
      tree.collapseRow(i);
    }
    setRootVisible(false);
  }

  public final DefaultMutableTreeNode getRootNode() {
    return root;
  }

  protected AnActionButtonRunnable getAddAction() {
    return null;
  }

  protected AnActionButtonRunnable getRemoveAction() {
    return null;
  }

  protected void initToolbarDecoratorExtra(ToolbarDecorator decorator) {
  }

  protected void addActionPanelExtra(@NotNull JPanel actionsPanel) {
  }

  /**
   * 返回的Panel的布局方式是BorderLayout
   *
   * @return 容器
   */
  public final JPanel createPanel() {
    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this)
      .setPreferredSize(new Dimension(-1, -1))
      .setAddAction(getAddAction())
      .setRemoveAction(getRemoveAction());
    initToolbarDecoratorExtra(decorator);
    JPanel panel = decorator.createPanel();
    addActionPanelExtra(decorator.getActionsPanel());
    return panel;
  }

  public DefaultMutableTreeNode getLastSelectedNode() {
    return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
  }

  // ===================================== Static Utility Methods ====================================

  @SuppressWarnings("unchecked")
  public T getSelectedItem() {
    DefaultMutableTreeNode node = getLastSelectedNode();
    return node == null ? null : (T) node.getUserObject();
  }

  public void setRoot(DefaultMutableTreeNode rootNode) {
    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    root.removeAllChildren();
    root = rootNode;
    model.setRoot(rootNode);
    // 刷新整棵树
    model.reload();
  }

  public void setRoot(T rootObject) {
    setRoot(new DefaultMutableTreeNode(rootObject));
  }
}
