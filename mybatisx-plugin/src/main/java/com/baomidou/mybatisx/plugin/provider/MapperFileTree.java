package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.plugin.components.Label;
import com.baomidou.mybatisx.plugin.components.ScrollPane;
import com.baomidou.mybatisx.plugin.components.TreeModel;
import com.baomidou.mybatisx.plugin.components.TreeView;
import com.baomidou.mybatisx.util.ObjectUtils;
import com.intellij.openapi.fileChooser.tree.FileTreeModel;
import com.intellij.psi.PsiElement;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.render.LabelBasedRenderer;
import com.intellij.util.containers.ArrayListSet;
import com.intellij.util.containers.MultiMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.util.Collection;

class MapperFileTree extends ScrollPane {

  TreeView<MapperFile> treeView;
  MultiMap<String, PsiElement> statementMap;

  public MapperFileTree() {
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.treeView = new TreeView<>() {
      @Override
      protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
          @Override
          public void run(AnActionButton anActionButton) {

          }
        };
      }
    };

    this.treeView.setRootVisible(false);
    this.treeView.setCellRenderer(new MapperStatementCellRender());
    this.treeView.setModel(new MapperTreeModel(this.treeView));

    this.statementMap = new MultiMap<>() {
      @Override
      protected @NotNull Collection<PsiElement> createEmptyCollection() {
        return new ArrayListSet<>();
      }
    };

    this.setContent(this.treeView);
  }

  public void addStatement(PsiElement psiElement) {
    String name = psiElement.getContainingFile().getName();
    Collection<PsiElement> psiElements = statementMap.get(name);
    if (psiElements.contains(psiElement)) {
      return;
    }
    psiElements.add(psiElement);

    MapperFile mapperFile = new MapperFile();
    mapperFile.filename = psiElement.getContainingFile().getName();
    mapperFile.element = psiElement;

    this.treeView.addChild(mapperFile);
  }

  static class MapperStatementCellRender extends LabelBasedRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

      return new Label(ObjectUtils.toString(value));
    }
  }

  /**
   * @see FileTreeModel
   */
  static class MapperTreeModel extends TreeModel<MapperFile> {

    public MapperTreeModel(TreeView<MapperFile> treeView) {
      super(treeView);
    }
  }

  static class StatementNode {

    SqlCommandType sqlCommandType;
  }
}
