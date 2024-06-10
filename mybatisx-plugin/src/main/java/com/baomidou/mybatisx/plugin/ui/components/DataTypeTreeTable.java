package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.JBTreeTableView;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeColumnInfo;
import com.intellij.util.ui.ColumnInfo;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
 */
public class DataTypeTreeTable extends JBTreeTableView<DataTypeItem> {

    DataTypeNode root;

    public DataTypeTreeTable() {
        super(new Model(DataTypeNode.ROOT));
        this.root = (DataTypeNode) treeTableModel.getRoot();

        DataTypeNode javaGroup = new DataTypeNode(new DataTypeItem("java"));

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

        getTree().expandPath(new TreePath(root));
        // 不展示根节点
        getTree().setRootVisible(false);

        setShowGrid(true);
        setShowColumns(true);
    }

    /**
     * @see com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns
     */
    static class Model extends ListTreeTableModelOnColumns {

        public Model(TreeNode root) {
            super(root, new ColumnInfo[]{
                    new TreeColumnInfo("")
                    , new ColumnInfo<DefaultMutableTreeNode, String>("类型名称") {
                    @Override
                    public @Nullable String valueOf(DefaultMutableTreeNode node) {
                        if (node instanceof DataTypeNode) {
                            return ((DataTypeNode) node).getItem().getIdentifier();
                        }
                        return null;
                    }
                }}
            );
        }

        @Override
        public Object getValueAt(Object value, int column) {
            System.out.println(value.getClass());
            if (column == 0) {
                return null;
            }
            return super.getValueAt(value, column);
        }
    }

    @Getter
    static class DataTypeNode extends DefaultMutableTreeNode {

        public static final DataTypeNode ROOT = new DataTypeNode(new DataTypeItem("Root"));

        @NotNull DataTypeItem item;

        DataTypeNode(@NotNull DataTypeItem item) {
            super(item);
            this.item = item;
        }

        public void addDataType(DataTypeNode dataTypeNode) {
            if (dataTypeNode == null) {
                return;
            }
            add(dataTypeNode);
        }

        public void addChildDataType(String identifier) {
            add(new DataTypeNode(new DataTypeItem(this.item.getGroup(), identifier)));
        }
    }
}
