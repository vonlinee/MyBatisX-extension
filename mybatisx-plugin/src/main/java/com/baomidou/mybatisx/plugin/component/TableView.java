package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.ui.TableViewModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * 必须把JTable添加到JScrollPane控件中，否则默认不显示列标题
 */
public class TableView<T> extends com.intellij.ui.table.TableView<T> {

    public TableView() {
        super(new DefaultListTableModel<>());
    }

    protected AnActionButtonRunnable getAddAction() {
        return null;
    }

    protected AnActionButtonRunnable getRemoveAction() {
        return null;
    }

    protected void initToolbarDecoratorExtra(ToolbarDecorator decorator) {
    }

    protected void addActionPanelExtra(@NotNull JPanel actionsPanel) {
    }

    /**
     * 返回的Panel的布局方式是BorderLayout
     *
     * @return 容器
     */
    public final JPanel createPanel() {
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this)
            .setPreferredSize(new Dimension(-1, -1))
            .setAddAction(getAddAction())
            .setRemoveAction(getRemoveAction());
        initToolbarDecoratorExtra(decorator);
        JPanel panel = decorator.createPanel();
        addActionPanelExtra(decorator.getActionsPanel());
        return panel;
    }

    public final void addRow(T rowItem) {
        TableViewModel<T> model = getTableViewModel();
        if (model instanceof ListTableModel) {
            ((ListTableModel<T>) model).addRow(rowItem);
        }
    }

    public final void addRows(Collection<T> rowItems) {
        TableViewModel<T> model = getTableViewModel();
        if (model instanceof ListTableModel) {
            ((ListTableModel<T>) model).addRows(rowItems);
        }
    }

    @Override
    public DefaultListTableModel<T> getModel() {
        return (DefaultListTableModel<T>) super.getListTableModel();
    }

    public void clearAllRows() {
        DefaultListTableModel<T> listTableModel = getModel();
        listTableModel.removeAllRows();
    }
}
