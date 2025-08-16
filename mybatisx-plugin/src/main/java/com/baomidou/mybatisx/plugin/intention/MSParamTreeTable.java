package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.components.EnumComboBox;
import com.baomidou.mybatisx.plugin.components.TextField;
import com.baomidou.mybatisx.plugin.components.TreeTableView;
import com.baomidou.mybatisx.plugin.ui.UIHelper;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExpandableTextField;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTableCellRenderer;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MSParamTreeTable extends TreeTableView<ParamNode> {

  private static final int VALUE_COLUMN_INDEX = 2;

  private final TableModel model;

  private final ColumnInfo<ParamNode, ?>[] columns;

  @SuppressWarnings("unchecked")
  public MSParamTreeTable() {
    super(new TableModel(new ParamNode("root", null, ParamDataType.UNKNOWN),
      new ColumnInfo[]{
        new ColumnInfo<ParamNode, String>("Name") {
          @Override
          public @Nullable String valueOf(ParamNode paramNode) {
            return paramNode.getKey();
          }

          @Override
          public Class<?> getColumnClass() {
            return TreeTableModel.class;
          }

          @Override
          public @NotNull TableCellEditor getEditor(ParamNode paramNode) {
            TextField textField = new TextField();
            DefaultCellEditor editor = new DefaultCellEditor(textField);
            editor.addCellEditorListener(new CellEditorListener() {
              @Override
              public void editingStopped(ChangeEvent e) {
                String s = UIHelper.getCellEditorStringValue(e);
                paramNode.setKey(s);
                textField.setToolTipText(s);
              }

              @Override
              public void editingCanceled(ChangeEvent e) {
              }
            });
            return editor;
          }
        },
        new ColumnInfo<ParamNode, ParamDataType>("Type") {

          @Override
          public @Nullable ParamDataType valueOf(ParamNode paramNode) {
            return paramNode.getDataType();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            EnumComboBox<ParamDataType> paramTypeComboBox = new EnumComboBox<>(ParamDataType.class);
            DefaultCellEditor editor = new DefaultCellEditor(paramTypeComboBox);
            editor.addCellEditorListener(new CellEditorListener() {
              @Override
              public void editingStopped(ChangeEvent e) {
                paramNode.setDataType(UIHelper.getCellEditorStringValue(e));
              }

              @Override
              public void editingCanceled(ChangeEvent e) {
              }
            });
            return editor;
          }
        },
        /*new ColumnInfo<ParamNode, String>("JdbcType") {

          @Override
          public @Nullable String valueOf(ParamNode paramNode) {
            return paramNode.getJdbcType();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            EnumComboBox<JdbcType> comboBox = new EnumComboBox<>(JdbcType.class);
            DefaultCellEditor editor = new DefaultCellEditor(comboBox);
            editor.addCellEditorListener(new CellEditorListener() {
              @Override
              public void editingStopped(ChangeEvent e) {
                paramNode.setJdbcType(UIHelper.getCellEditorStringValue(e));
              }

              @Override
              public void editingCanceled(ChangeEvent e) {
              }
            });
            return editor;
          }
        },*/
        new ColumnInfo<ParamNode, String>("Value") {

          @Override
          public @Nullable String valueOf(ParamNode paramNode) {
            return paramNode.getValue();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            JBTextField textField;
            if (paramNode.getDataType().isArray()) {
              textField = new ExpandableTextField();
            } else {
              textField = new JBTextField();
            }
            DefaultCellEditor editor = new DefaultCellEditor(textField);
            editor.addCellEditorListener(new CellEditorListener() {
              @Override
              public void editingStopped(ChangeEvent e) {
                String s = UIHelper.getCellEditorStringValue(e);
                paramNode.setValue(s);
                textField.setToolTipText(s);
              }

              @Override
              public void editingCanceled(ChangeEvent e) {
              }
            });
            return editor;
          }
        },
      }));
    this.model = (TableModel) getTreeTableModel();
    this.columns = this.model.getColumns();

    TableColumn column = getColumnModel().getColumn(1);
    SwingUtils.setFixedWidth(column, 110);

    setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    JTree tree = this.getTree();
    tree.setShowsRootHandles(true);
  }

  @Override
  public TreeTableCellRenderer createTableRenderer(TreeTableModel treeTableModel) {
    TreeTableCellRenderer tableRenderer = super.createTableRenderer(treeTableModel);
    tableRenderer.setRootVisible(false);
    tableRenderer.setShowsRootHandles(true);
    return tableRenderer;
  }

  @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
    TreePath treePath = getTree().getPathForRow(row);
    if (treePath == null) {
      return super.getCellRenderer(row, column);
    }
    Object node = treePath.getLastPathComponent();
    TableCellRenderer renderer = columns[column].getRenderer((ParamNode) node);
    return renderer == null ? super.getCellRenderer(row, column) : renderer;
  }

  @Override
  public TableCellEditor getCellEditor(int row, int column) {
    TreePath treePath = getTree().getPathForRow(row);
    if (treePath == null) {
      return super.getCellEditor(row, column);
    }
    Object node = treePath.getLastPathComponent();
    TableCellEditor editor = columns[column].getEditor((ParamNode) node);
    return editor == null ? super.getCellEditor(row, column) : editor;
  }

  public void setAll(List<ParamNode> params) {
    ParamNode rootNode = (ParamNode) getTreeTableModel().getRoot();
    rootNode.setChildren(params);
    for (ParamNode param : params) {
      rootNode.add(param);
    }
    final TreeTableTree tree = getTree();
    TreeNode[] path = rootNode.getLastLeaf().getPath();
    tree.scrollPathToVisible(new TreePath(path));
    TableModel model = (TableModel) getTreeTableModel();
    model.nodeStructureChanged(rootNode);
    SwingUtils.expandAll(tree);
  }

  public void resetAll(List<ParamNode> params) {
    DefaultTreeModel treeModel = (DefaultTreeModel) getTree().getModel();
    ParamNode root = (ParamNode) treeModel.getRoot();
    for (ParamNode child : root.getChildren()) {
      for (ParamNode newChild : params) {
        if (Objects.equals(child.getKey(), newChild.getKey())) {
          updateValue(root, child, newChild);
        }
      }
    }
    treeModel.nodeChanged(root);
    SwingUtils.expandAll(getTree());
  }

  private void updateValue(ParamNode parent, ParamNode old, ParamNode newNode) {
    if (old.hasChildren()) {
      for (ParamNode child : old.getChildren()) {
        boolean found = false;
        for (ParamNode newNodeChild : newNode.getChildren()) {
          if (Objects.equals(child.getKey(), newNodeChild.getKey())) {
            updateValue(old, child, newNodeChild);
            found = true;
            break;
          }
        }
        if (!found) {
          // 新增的节点
          parent.addChild(newNode);
        }
      }
    } else {
      // 尽量不修改已经确定好的类型
      if (old.getDataType() == ParamDataType.STRING && "null".equalsIgnoreCase(newNode.getValue())) {
        old.setValue("");
      } else {
        old.setValue(newNode.getValue());
      }
      if (old.getDataType() == ParamDataType.UNKNOWN) {
        old.setDataType(newNode.getDataType().toString());
      } else if (newNode.getDataType() != null && newNode.getDataType() != ParamDataType.UNKNOWN) {
        old.setDataType(newNode.getDataType().toString());
      }
    }
  }

  /**
   * 将参数名转化为嵌套Map形式
   *
   * @return 扁平化Map
   */
  public Map<String, Object> getParamsAsMap() {
    Map<String, Object> map = new HashMap<>();
    ParamNode root = (ParamNode) getTree().getModel().getRoot();
    for (int i = 0; i < root.getChildCount(); i++) {
      ParamNode child = (ParamNode) root.getChildAt(i);
      LinkedList<String> list = new LinkedList<>();
      fillParamMap(list, child, map);
    }
    return map;
  }

  private void fillParamMap(LinkedList<String> list, ParamNode node, Map<String, Object> map) {
    if (node.getChildCount() == 0) {
      list.add(node.getKey());
      StringBuilder errMsg = new StringBuilder();
      Object value = node.getDataType().parseObject(node.getValue(), errMsg);
      map.put(String.join(".", list), value);
      list.removeLast();
    } else {
      list.add(node.getKey());
      for (int i = 0; i < node.getChildCount(); i++) {
        ParamNode child = (ParamNode) node.getChildAt(i);
        fillParamMap(list, child, map);
      }
    }
  }

  private Map<String, Object> getParamsAsFlattenMap() {
    Map<String, Object> map = new HashMap<>();
    TableModel tableModel = (TableModel) this.getModel();
    int rowCount = 10;
    Map<String, ParamDataType> dataTypeNameMap = ParamDataType.asMap();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < rowCount; i++) {
      String dataType = (String) tableModel.getValueAt(i, 2);
      ParamDataType type = dataTypeNameMap.getOrDefault(dataType, ParamDataType.UNKNOWN);
      String value = (String) tableModel.getValueAt(i, 1);
      map.put((String) tableModel.getValueAt(i, 0), type.parseObject(value, sb));
    }
    return map;
  }

  private static class TableModel extends ListTreeTableModelOnColumns {

    protected TableModel(TreeNode root, ColumnInfo[] columns) {
      super(root, columns);
    }

    @Override
    public Object getValueAt(Object value, int column) {
      if (!(value instanceof ParamNode)) {
        return null;
      }
      ParamNode row = (ParamNode) value;
      if (column != 0 && row.getChildCount() > 0) {
        return null;
      }
      return super.getValueAt(value, column);
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
      if (!(node instanceof DefaultMutableTreeNode)) {
        return false;
      }
      DefaultMutableTreeNode row = (DefaultMutableTreeNode) node;
      return row.getChildCount() <= 0;
    }
  }
}
