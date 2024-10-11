package com.baomidou.mybatisx.plugin.component;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Collection;

public class TitledListPane<E> extends BorderPane {
    @NotNull
    Label titleLabel;
    @NotNull
    ListView<E> listView;

    public TitledListPane(String title, Collection<E> list) {
        // 标题
        titleLabel = new Label(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setPreferredSize(0, 50);
        // 列表
        listView = createListView(list);
        listView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listView.setVisibleRowCount(-1); // 自适应高度

        ScrollPane scrollPane = new ScrollPane(listView.createPanel());
        DefaultListModel<E> listModel = listView.getListModel();
        listModel.addAll(list);

        addToTop(titleLabel);
        addToCenter(scrollPane);
    }

    protected ListView<E> createListView(Collection<E> list) {
        return new ListView<>();
    }

    public ListView<E> getListView() {
        return listView;
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        this.listView.addListSelectionListener(listSelectionListener);
    }

    public E getSelectedItem() {
        return listView.getSelectedValue();
    }

    public void setAll(Collection<E> items) {
        if (items == null) {
            return;
        }
        DefaultListModel<E> listModel = listView.getListModel();
        listModel.clear();
        listModel.addAll(items);
    }

    public void setCellRenderer(ListCellRenderer<? super E> cellRenderer) {
        listView.setCellRenderer(cellRenderer);
    }
}
