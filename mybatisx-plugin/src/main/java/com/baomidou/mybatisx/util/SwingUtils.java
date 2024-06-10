package com.baomidou.mybatisx.util;

import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;

public final class SwingUtils {

    private SwingUtils() {
    }

    public static JPanel newBoxLayoutPanel() {
        return newBoxLayoutPanel(false);
    }

    public static JPanel newBoxLayoutPanel(boolean vertical) {
        JPanel jPanel = new JPanel();
        BoxLayout layout = new BoxLayout(jPanel, vertical ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS);
        jPanel.setLayout(layout);
        return jPanel;
    }

    public static Icon getIcon(String name) {
        name = "/icons/" + name;
        return IconLoader.getIcon(name);
    }

    public static void setPreferredWidth(JComponent component, int width) {
        component.setPreferredSize(new Dimension(width, component.getPreferredSize().height));
    }

    public static void setPreferredHeight(JComponent component, int height) {
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, height));
    }

    public static void setEmptyBorder(JComponent component) {
        component.setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * 创建一个图标按钮
     *
     * @param icon 图标路径
     * @return 图标按钮
     */
    public static JButton newIconButton(String icon) {
        JButton button = new JButton();
        Icon iconNode = getIcon(icon);
        button.setIcon(iconNode);
        button.setHorizontalTextPosition(JButton.CENTER); // 文本位置设置为居中（如果不需要文本，可以忽略此行代码）
        button.setVerticalTextPosition(JButton.CENTER); // 文本位置设置为居中（如果不需要文本，可以忽略此行代码）
        button.setText(""); // 如果不需要文本，确保将文本设置为空字符串
        // 为了确保图标居中，可以调整按钮的Insets
        Insets insets = button.getInsets();
        int iconWidth = iconNode.getIconWidth();
        int iconHeight = iconNode.getIconHeight();
        int buttonWidth = button.getWidth();
        int buttonHeight = button.getHeight();
        int hGap = (buttonWidth - iconWidth) / 2;
        int vGap = (buttonHeight - iconHeight) / 2;
        button.setMargin(JBUI.insets(vGap, hGap, vGap, hGap)); // 设置边距来居中图标
        return button;
    }

    /**
     * 设置固定宽度
     *
     * @param column 列
     * @param width  列宽度
     */
    public static void setFixedWidth(TableColumn column, int width) {
        column.setMinWidth(width);
        column.setMaxWidth(width);
        column.setWidth(width);
    }

    public static Dimension getScreenBasedDimension(double wr, double hr) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) (screenSize.getWidth() * wr), (int) (screenSize.getHeight() * hr));
    }

    /**
     * 根据屏幕尺寸适应的大小
     *
     * @param r 宽度和高度比例
     * @return 尺寸大小
     */
    public static Dimension getScreenBasedDimension(double r) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int) (screenSize.getWidth() * r), (int) (screenSize.getHeight() * r));
    }

    public static void invokeLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    @Nullable
    public static DefaultMutableTreeNode getSelectedNode(JTree tree) {
        if (tree == null) {
            return null;
        }
        return (DefaultMutableTreeNode) tree.getSelectionModel().getSelectionPath().getLastPathComponent();
    }

    /**
     * 复制内容到系统剪贴板
     *
     * @param content 文本内容
     */
    public static void copyToClipboard(@NotNull String content) {
        // 封装文本内容
        StringSelection selection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(selection, selection);
    }

    /**
     * 鼠标左键单击
     *
     * @param event MouseEvent
     * @return 是否鼠标左键单击
     */
    public static boolean isLeftClicked(MouseEvent event) {
        return SwingUtilities.isLeftMouseButton(event);
    }

    /**
     * 鼠标右键单击
     *
     * @param event MouseEvent
     * @return 是否鼠标右键单击
     */
    public static boolean isRightClicked(MouseEvent event) {
        return SwingUtilities.isRightMouseButton(event);
    }

    /**
     * Creates a horizontal glue component.
     *
     * @return the component
     * @see Box#createHorizontalGlue()
     */
    public static Component createHorizontalGlue(int w) {
        // Dimension min, Dimension pref, Dimension max
        return new Box.Filler(new Dimension(0, 0), new Dimension(w, 0),
            new Dimension(Short.MAX_VALUE, 0));
    }

    /**
     * Creates a vertical glue component.
     *
     * @return the component
     */
    public static Component createVerticalGlue(int h) {
        // Dimension min, Dimension pref, Dimension max
        return new Box.Filler(new Dimension(0, 0), new Dimension(0, h),
            new Dimension(0, Short.MAX_VALUE));
    }
}
