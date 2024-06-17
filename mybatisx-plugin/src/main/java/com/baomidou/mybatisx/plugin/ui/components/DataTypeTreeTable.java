package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.JBTreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 数据类型表
 *
 * @see com.intellij.openapi.vfs.encoding.FileEncodingConfigurable
 */
public class DataTypeTreeTable extends JBTreeTableView<DataTypeItem> {

    DataTypeNode root;

    public DataTypeTreeTable() {
        super(new Model(DataTypeNode.ROOT));
        this.root = (DataTypeNode) treeTableModel.getRoot();

        DataTypeNode javaGroup = new DataTypeNode("Java");

        javaGroup.addChildDataType(int.class.getName());
        javaGroup.addChildDataType(short.class.getName());
        javaGroup.addChildDataType(byte.class.getName());
        javaGroup.addChildDataType(char.class.getName());
        javaGroup.addChildDataType(long.class.getName());
        javaGroup.addChildDataType(double.class.getName());
        javaGroup.addChildDataType(float.class.getName());
        javaGroup.addChildDataType(boolean.class.getName());

        javaGroup.addChildDataType(Integer.class.getName());
        javaGroup.addChildDataType(Short.class.getName());
        javaGroup.addChildDataType(Byte.class.getName());
        javaGroup.addChildDataType(Character.class.getName());
        javaGroup.addChildDataType(Long.class.getName());
        javaGroup.addChildDataType(Double.class.getName());
        javaGroup.addChildDataType(Float.class.getName());
        javaGroup.addChildDataType(Boolean.class.getName());

        javaGroup.addChildDataType(BigDecimal.class.getName());
        javaGroup.addChildDataType(String.class.getName());
        javaGroup.addChildDataType(CharSequence.class.getName());
        javaGroup.addChildDataType(BigInteger.class.getName());
        javaGroup.addChildDataType(Date.class.getName());
        javaGroup.addChildDataType(LocalDateTime.class.getName());
        javaGroup.addChildDataType(LocalDate.class.getName());

        root.add(javaGroup);

        getTree().setShowsRootHandles(true);

        // 不展示根节点
        getTree().setRootVisible(false);

        setShowGrid(true);
        setShowColumns(true);

        getTree().expandPath(new TreePath(root));
    }

    /**
     * @see com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns
     */
    static class Model extends ListTreeTableModelOnColumns {

        public Model(TreeNode root) {
            super(root, new ColumnInfo[]{new ColumnInfo<DefaultMutableTreeNode, String>("A") {

                @Nullable
                @Override
                public String valueOf(DefaultMutableTreeNode node) {
                    return null;
                }

                /**
                 * @see com.intellij.ui.table.JBTable#getCellRenderer(int, int)
                 * @param o 节点
                 * @param renderer 单元格渲染器
                 * @return
                 */
                @Override
                public TableCellRenderer getCustomizedRenderer(DefaultMutableTreeNode o, TableCellRenderer renderer) {
                    return super.getCustomizedRenderer(o, renderer);
                }

                @Override
                public Class<?> getColumnClass() {
                    return TreeTableModel.class;
                }
            }, new ColumnInfo<DefaultMutableTreeNode, String>("类型名称") {
                @Override
                public @Nullable String valueOf(DefaultMutableTreeNode node) {
                    if (node instanceof DataTypeNode) {
                        return ((DataTypeNode) node).getItem().getIdentifier();
                    }
                    return null;
                }
            }});
        }

        @Override
        public Object getValueAt(Object value, int column) {
            if (column == 0) {
                return null;
            }
            return super.getValueAt(value, column);
        }
    }
}
