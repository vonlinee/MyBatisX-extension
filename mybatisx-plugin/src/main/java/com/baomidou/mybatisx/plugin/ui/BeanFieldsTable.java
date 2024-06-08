package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.model.Field;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BeanFieldsTable extends JScrollPane {

    JTable table;

    public BeanFieldsTable() {
        super(new JTable());
        this.table = (JTable) getViewport().getView();

        table.setModel(new FieldTableTableModel());

        table.setShowGrid(false);
        table.setIntercellSpacing(JBUI.emptySize());
        table.setDragEnabled(false);
        table.setShowVerticalLines(false);

        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 自定义渲染器
        TableColumn operationColumn = table.getColumnModel().getColumn(3);
        SwingUtils.setFixedWidth(operationColumn, 60);
        operationColumn.setCellRenderer(new OperationColumn(table));
        operationColumn.setCellEditor(new OperationColumn(table));
    }

    public final void addRow(String fieldName, String fieldValue, String dataType) {
        FieldTableTableModel model = getTableModel();
        model.addRow(new Object[]{fieldName, fieldValue, dataType});
    }

    /**
     * 追加一个PsiClass的所有字段
     *
     * @param psiClass PSI类
     */
    public void appendFieldsOfPsiClass(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();
        for (PsiField field : fields) {
            // 字段初始化表达式
            PsiExpression initializer = field.getInitializer();
            String text = null;
            if (initializer != null) {
                text = initializer.getText();
            }
            PsiType type = field.getType();
            String typeText = type.getCanonicalText();
            this.addRow(field.getName(), text, typeText);
        }
    }

    public final void addRows(List<Object[]> rows) {
        FieldTableTableModel model = getTableModel();
        for (Object[] row : rows) {
            model.addRow(row);
        }
    }

    public Vector<?> getRows() {
        FieldTableTableModel model = getTableModel();
        return model.getDataVector();
    }

    public List<Field> getFields() {
        FieldTableTableModel tableModel = getTableModel();
        Vector<Vector> rows = tableModel.getDataVector();
        List<Field> fields = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Vector row = rows.get(i);
            Field field = new Field(String.valueOf(row.get(0)), String.valueOf(row.get(1)), false, String.valueOf(row.get(2)));
            fields.add(field);
        }
        return fields;
    }

    private FieldTableTableModel getTableModel() {
        return (FieldTableTableModel) this.table.getModel();
    }

    /**
     * 自定义一个往列里边添加按钮的单元格编辑器。最好继承DefaultCellEditor
     */
    private static class OperationColumn extends DefaultCellEditor implements TableCellRenderer {

        JTable fieldsTable;
        private JPanel panel;
        private JButton btnDelete;
        private JButton btnAdd;
        private int row;
        private int column;

        public OperationColumn(JTable jTable) {
            // DefaultCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
            super(new JTextField());

            this.fieldsTable = jTable;

            // 设置点击几次激活编辑。
            this.setClickCountToStart(1);
            this.initButton();
            this.initPanel();
        }

        private void initButton() {
            this.btnDelete = SwingUtils.newIconButton("delete.svg");
            // 将边框外的上下左右空间设置为0
            this.btnDelete.setMargin(JBUI.emptyInsets());
            // 将标签中显示的文本和图标之间的间隔量设置为0
            this.btnDelete.setIconTextGap(0);
            // 不打印边框
            this.btnDelete.setBorderPainted(false);
            // 除去边框
            this.btnDelete.setBorder(null);
            this.btnDelete.setFocusPainted(false);//除去焦点的框
            this.btnDelete.setContentAreaFilled(false);//除去默认的背景填充
            // 为按钮添加事件。这里只能添加ActionListener事件，Mouse事件无效。
            this.btnDelete.addActionListener(e -> {
                // 触发取消编辑的事件，不会调用tableModel的setValue方法。
                // OperationColumn.this.fireEditingCanceled();
                FieldTableTableModel model = (FieldTableTableModel) fieldsTable.getModel();
                // 不取消编辑会出现索引越界错误
                cancelCellEditing();
                int selectedRow = fieldsTable.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                }
            });
            btnDelete.setPreferredSize(new Dimension(20, btnDelete.getPreferredSize().height)); // 设置按钮宽度为25
            // 设置按钮的边框为空
            btnDelete.setBorder(BorderFactory.createEmptyBorder());

            btnDelete.setOpaque(true);
            btnDelete.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btnDelete.setAlignmentY(JButton.CENTER_ALIGNMENT);

            btnAdd = SwingUtils.newIconButton("copy.svg");
            btnAdd.setIconTextGap(0);
            // 将边框外的上下左右空间设置为0
            btnAdd.setMargin(JBUI.emptyInsets());
            // 将标签中显示的文本和图标之间的间隔量设置为0
            btnAdd.setIconTextGap(0);
            // 不打印边框
            btnAdd.setBorderPainted(false);

            // 除去边框
            btnAdd.setBorder(null);
            btnAdd.setOpaque(true);
            btnAdd.setFocusPainted(false);//除去焦点的框
            btnAdd.setContentAreaFilled(false);//除去默认的背景填充
            btnAdd.addActionListener(e -> {
                String[] rowValues = {null, null, null};
                DefaultTableModel model = (DefaultTableModel) fieldsTable.getModel();
                model.addRow(rowValues);  // 添加一行
            });
            btnAdd.setBorder(BorderFactory.createEmptyBorder());
            btnAdd.setPreferredSize(new Dimension(20, btnDelete.getPreferredSize().height)); // 设置按钮宽度为25
            btnAdd.setAlignmentX(JButton.CENTER_ALIGNMENT);
            btnAdd.setAlignmentY(JButton.CENTER_ALIGNMENT);
        }

        private void initPanel() {
            this.panel = new JPanel();
            this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.X_AXIS));
            // 添加按钮
            this.panel.add(SwingUtils.createHorizontalGlue(2));
            this.panel.add(this.btnAdd);

            this.panel.add(this.btnDelete);
            this.panel.add(SwingUtils.createHorizontalGlue(2));
            this.panel.add(Box.createHorizontalGlue());
            panel.setAlignmentX(Component.CENTER_ALIGNMENT); // 水平居中
            panel.setAlignmentY(Component.CENTER_ALIGNMENT); // 垂直居中
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }

        /**
         * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.column = column;
            // 只为按钮赋值即可。也可以作其它操作。
            return this.panel;
        }

        /**
         * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
         */
        @Override
        public Object getCellEditorValue() {
            return this.btnDelete.getText();
        }
    }

    private static class FieldTableTableModel extends DefaultTableModel {

        public FieldTableTableModel() {
            super(null, new String[]{"Name", "Value", "Type", "Action"});
        }
    }
}
