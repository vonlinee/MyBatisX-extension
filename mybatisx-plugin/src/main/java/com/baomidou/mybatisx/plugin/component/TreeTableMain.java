package com.baomidou.mybatisx.plugin.component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TreeTableMain extends JFrame {

  public TreeTableMain() {
    super("swing树表");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(0, 1));

    MyAbstractTreeTableModel treeTableModel = new MyDataModel(createDataStructure());
    MyTreeTable myTreeTable = new MyTreeTable(treeTableModel);

    Container cPane = getContentPane();
    cPane.add(new JScrollPane(myTreeTable));
    setSize(1000, 800);
    setLocationRelativeTo(null);
  }

  private static MyDataNode createDataStructure() {
    //娄底下的子节点
    List<MyDataNode> louDi = new ArrayList<MyDataNode>();
    louDi.add(new MyDataNode("新化县", "320000", "18万平方公里", "2002", "亚热带季风气候", null));

    //湖南下的子节点
    List<MyDataNode> children1 = new ArrayList<MyDataNode>();
    children1.add(new MyDataNode("长沙", "111111", "10万平方公里", "20000", "亚热带季风气候", null));
    children1.add(new MyDataNode("娄底", "222222", "22万平方公里", "20000", "亚热带季风气候", louDi));

    //江西下的子节点
    List<MyDataNode> children2 = new ArrayList<MyDataNode>();
    children2.add(new MyDataNode("南昌", "333333", "11万平方公里", "20000", "亚热带季风气候", null));
    children2.add(new MyDataNode("九江", "444444", "23万平方公里", "20000", "亚热带季风气候", null));

    //上海下的子节点
    List<MyDataNode> children3 = new ArrayList<>();
    children3.add(new MyDataNode("黄埔", "555555", "13万平方公里", "20000", "亚热带季风气候", null));
    children3.add(new MyDataNode("嘉定", "666666", "25万平方公里", "20000", "亚热带季风气候", null));

    //父节点下第一级子节点
    List<MyDataNode> rootNodes = new ArrayList<>();
    rootNodes.add(new MyDataNode("湖南", "1564644", "80万平方公里", "20000", "亚热带季风气候", children1));
    rootNodes.add(new MyDataNode("江西", "5421312", "90万平方公里", "20000", "亚热带季风气候", children2));
    rootNodes.add(new MyDataNode("上海", "2135465", "30万平方公里", "254654", "亚热带季风气候", children3));
    //父节点
    return new MyDataNode("中国", "140000000", "9800000万平方公里", "10000", "亚热带季风气候", rootNodes);
  }

  public static void main(final String[] args) {
    Runnable gui = () -> {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      new TreeTableMain().setVisible(true);
    };
    SwingUtilities.invokeLater(gui);
  }
}
