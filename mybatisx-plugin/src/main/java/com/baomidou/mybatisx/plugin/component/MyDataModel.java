package com.baomidou.mybatisx.plugin.component;

/**
 * 该类定义视图的具体数据模型。这意味着定义了包含数据类型的列。
 * 此外，该类还包含尚未实现的接口方法。请注意方法 。这必须返回值，以便它们可以对一个或
 * 在此方法中，您还可以仅返回第一列 （） 的值，否则返回值
 */
public class MyDataModel extends MyAbstractTreeTableModel {
  // 列名.
  static protected String[] columnNames = {"地区名", "人口", "面积", "GDP", "所属季风带"};

  // 列类型.
  static protected Class<?>[] columnTypes = {MyTreeTableModel.class, String.class, String.class, String.class, String.class};

  public MyDataModel(MyDataNode rootNode) {
    super(rootNode);
    root = rootNode;
  }

  @Override
  public Object getChild(Object parent, int index) {
    return ((MyDataNode) parent).getChildren().get(index);
  }

  @Override
  public int getChildCount(Object parent) {
    return ((MyDataNode) parent).getChildren().size();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int column) {
    return columnNames[column];
  }

  @Override
  public Class<?> getColumnClass(int column) {
    return columnTypes[column];
  }

  @Override
  public Object getValueAt(Object node, int column) {
    switch (column) {
      case 0:
        return ((MyDataNode) node).getName();
      case 1:
        return ((MyDataNode) node).getPopulation();
      case 2:
        return ((MyDataNode) node).getArea();
      case 3:
        return ((MyDataNode) node).getGDP();
      case 4:
        return ((MyDataNode) node).getMonsoonClimate();
      default:
        break;
    }
    return null;
  }

  @Override
  public boolean isCellEditable(Object node, int column) {
    return true; // 激活TreeExpandListener很重要
  }

  @Override
  public void setValueAt(Object aValue, Object node, int column) {
  }
}
