package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.plugin.component.TreeView;
import com.intellij.util.ui.EditableTreeModel;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Collection;

/**
 * 模板树形结构
 */
public class TemplateTreeView extends TreeView<TemplateInfo> {

    public TemplateTreeView() {
        super();
        setModel(new TemplateTreeViewModel(getRoot()));
    }

    static class TemplateTreeViewModel extends DefaultTreeModel implements EditableTreeModel {

        public TemplateTreeViewModel(TreeNode root) {
            super(root);
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
