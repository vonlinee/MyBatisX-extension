package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.component.CustomComboBox;
import com.baomidou.mybatisx.plugin.component.EnumComboBox;
import com.baomidou.mybatisx.plugin.component.JBTreeTableView;
import com.baomidou.mybatisx.plugin.ui.UIHelper;
import com.baomidou.mybatisx.plugin.ui.dialog.DialogBase;
import com.baomidou.mybatisx.plugin.ui.dialog.TextInputDialog;
import com.baomidou.mybatisx.tip.JdbcType;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTableCellRenderer;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MSParamTreeTable extends JBTreeTableView<ParamNode> {

  private final TableModel model;

  private final ColumnInfo<ParamNode, ?>[] columns;

  @SuppressWarnings("unchecked")
  public MSParamTreeTable(Project project) {
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
        },
        new ColumnInfo<ParamNode, ParamDataType>("Type") {

          @Override
          public @Nullable ParamDataType valueOf(ParamNode paramNode) {
            return paramNode.getDataType();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            CustomComboBox<ParamDataType> customComboBox = new CustomComboBox<>();
            customComboBox.addItems(ParamDataType.values());

            DefaultCellEditor editor = new DefaultCellEditor(customComboBox);
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
        new ColumnInfo<ParamNode, String>("JdbcType") {

          @Override
          public @Nullable String valueOf(ParamNode paramNode) {
            return paramNode.getJdbcType();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            EnumComboBox<JdbcType> comboBox = new EnumComboBox<>();
            comboBox.addItems(JdbcType.values());
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
        },
        new ColumnInfo<ParamNode, String>("Value") {

          @Override
          public @Nullable String valueOf(ParamNode paramNode) {
            return paramNode.getValue();
          }

          @Override
          public TableCellEditor getEditor(ParamNode paramNode) {
            JBTextField textField = new JBTextField();
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
    JTree tree = this.getTree();

    tree.setShowsRootHandles(true);
  }

  public JPanel getPanel() {
    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this);
    decorator.setAddAction(anActionButton -> {
      ParamNode child = new ParamNode("param", "", ParamDataType.STRING);
      final TreeTableTree tree = getTree();
      TreePath selectionPath = getTree().getSelectionPath();
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
      // 通过JTable获取选中的行
      int selectedRow = getSelectedRow();
      // 通过行获取JTree中的TreePath
      TreePath selectedPath = getTree().getPathForRow(selectedRow);
      // 删除该Path
      removeSelectedPath(selectedPath);
      // 获取待删除节点的父节点
      ParamNode lastNode = (ParamNode) selectedPath.getLastPathComponent();
      TreeNode parent = lastNode.getParent();
      DefaultTreeModel treeModel = (DefaultTreeModel) getTree().getModel();
      treeModel.nodeStructureChanged(parent);
      treeModel.removeNodeFromParent(lastNode);
    });

    return decorator.createPanel();
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
    if (treePath == null) return super.getCellEditor(row, column);
    Object node = treePath.getLastPathComponent();
    TableCellEditor editor = columns[column].getEditor((ParamNode) node);
    return editor == null ? super.getCellEditor(row, column) : editor;
  }

  public final void setAll(List<ParamNode> params) {
    ParamNode rootNode = (ParamNode) getTreeTableModel().getRoot();
    rootNode.setChildren(params);
    for (ParamNode param : params) {
      rootNode.add(param);
    }

    final TreeTableTree tree = getTree();
    TreeNode[] path = rootNode.getLastLeaf().getPath();
    tree.scrollPathToVisible(new TreePath(path));
    model.nodeStructureChanged(rootNode);
    SwingUtils.expandAll(tree);
  }

  public final void resetAll(List<ParamNode> params) {
    DefaultTreeModel treeModel = (DefaultTreeModel) getTree().getModel();
    ParamNode paramNode = new ParamNode();
    for (ParamNode param : params) {
      paramNode.add(param);
    }
    treeModel.setRoot(paramNode);
    setAll(params);
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

  private Map<String, Object> getParamsAsMap1() {
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
      if (!(node instanceof ParamNode)) {
        return false;
      }
      ParamNode row = (ParamNode) node;
      return row.getChildCount() <= 0;
    }
  }
}
