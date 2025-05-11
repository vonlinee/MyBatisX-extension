package com.baomidou.mybatisx.plugin.ui.dialog;

import com.baomidou.mybatisx.feat.bean.BeanInfo;
import com.baomidou.mybatisx.plugin.ui.components.BeanFieldsTable;
import com.baomidou.mybatisx.plugin.ui.components.BeanToolPane;
import com.baomidou.mybatisx.plugin.ui.components.DDLCreatorTool;
import com.baomidou.mybatisx.util.IdeSDK;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BeanToolDialog extends DialogWrapper {

  Project project;
  JPanel mainPanel;
  BeanFieldsTable beanFieldsTable;
  JPanel bottomPanel;
  JBSplitter centerPanel;
  JTextField beanNameJTf;
  BeanToolPane beanToolPane;

  public BeanToolDialog(@Nullable Project project) {
    super(project);
    this.project = project;
    // 设置为非模态窗口
    setModal(false);
    setTitle("Bean Tool");
    init();
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    this.mainPanel = new JPanel();
    this.mainPanel.setLayout(new BorderLayout());

    JButton button = new JButton("Select JavaBean");
    button.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        IdeSDK.chooseClass(project, psiClass -> {
          beanNameJTf.setText(psiClass.getQualifiedName());
          beanFieldsTable.appendFieldsOfPsiClass(psiClass);
        });
      }
    });

    JPanel topPanel = SwingUtils.newHBoxLayoutPanel();
    topPanel.add(button);

    Box box = Box.createVerticalBox();

    Box beanNameBox = Box.createHorizontalBox();

    JLabel label = new JLabel("Bean Name: ");
    beanNameBox.add(label);
    beanNameBox.add(this.beanNameJTf = new JTextField());
    beanNameJTf.setMaximumSize(new Dimension(Integer.MAX_VALUE, beanNameJTf.getPreferredSize().height));

    box.add(beanNameBox);
    box.add(this.beanFieldsTable = new BeanFieldsTable());

    centerPanel = new JBSplitter(false, 0.4f);
    centerPanel.setFirstComponent(box);

    beanToolPane = new BeanToolPane();
    beanToolPane.addTool(new DDLCreatorTool(this.project));

    centerPanel.setSecondComponent(beanToolPane);

    this.mainPanel.add(centerPanel, BorderLayout.CENTER);
    this.mainPanel.add(topPanel, BorderLayout.NORTH);

    this.mainPanel.setPreferredSize(SwingUtils.getScreenBasedDimension(0.5));
    return this.mainPanel;
  }

  /**
   * 该样式会展示在会话框的最下方的位置
   *
   * @return Swing 组件
   */
  @Override
  protected JComponent createSouthPanel() {
    if (bottomPanel == null) {
      bottomPanel = new JPanel();
      BoxLayout layout = new BoxLayout(bottomPanel, BoxLayout.X_AXIS);
      bottomPanel.setLayout(layout);

      JButton btnGenerate = new JButton("生成");
      btnGenerate.addActionListener(e -> beanToolPane.doAction(new BeanInfo(null, beanFieldsTable.getFields())));
      bottomPanel.add(btnGenerate);
    }
    return bottomPanel;
  }

  @Override
  protected @NotNull JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
    return super.createButtonsPanel(buttons);
  }

  public void appendClassFields(PsiClass psiClass) {
    beanFieldsTable.appendFieldsOfPsiClass(psiClass);
  }

  public void showWithClass(PsiClass psiClass) {
    beanNameJTf.setText(psiClass.getQualifiedName());
    beanFieldsTable.appendFieldsOfPsiClass(psiClass);
    show();
  }
}
