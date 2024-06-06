package com.baomidou.plugin.idea.mybatisx.component;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/***
 * 在此类中，存储根节点，有一个方法可以检查子节点是否存在，并且管理所有事件侦听器。
 * 事件侦听器确保数据模型中的结构更改也转发到树并显示
 */
public abstract class MyAbstractTreeTableModel implements MyTreeTableModel {

    private static final int CHANGED = 0;
    private static final int INSERTED = 1;
    private static final int REMOVED = 2;
    private static final int STRUCTURE_CHANGED = 3;
    protected Object root;
    protected EventListenerList listenerList = new EventListenerList();

    public MyAbstractTreeTableModel(Object root) {
        this.root = root;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    /**
     * 这样不被调用.
     */
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    private void fireTreeNode(int changeType, Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = new TreeModelEvent(source, path, childIndices, children);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                switch (changeType) {
                    case CHANGED:
                        ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
                        break;
                    case INSERTED:
                        ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
                        break;
                    case REMOVED:
                        ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
                        break;
                    case STRUCTURE_CHANGED:
                        ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        fireTreeNode(CHANGED, source, path, childIndices, children);
    }

    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        fireTreeNode(INSERTED, source, path, childIndices, children);
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        fireTreeNode(REMOVED, source, path, childIndices, children);
    }

    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        fireTreeNode(STRUCTURE_CHANGED, source, path, childIndices, children);
    }
}
