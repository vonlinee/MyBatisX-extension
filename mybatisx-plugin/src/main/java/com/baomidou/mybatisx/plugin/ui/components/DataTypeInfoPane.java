package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.DataTypeSet;
import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.plugin.component.LabeledListCellRenderer;
import com.baomidou.mybatisx.plugin.component.ListView;
import com.baomidou.mybatisx.plugin.component.SplitPane;
import com.baomidou.mybatisx.plugin.component.TextFieldListCellRenderer;
import com.baomidou.mybatisx.plugin.component.TitledListPane;
import com.baomidou.mybatisx.plugin.setting.DataTypeSettings;
import com.baomidou.mybatisx.plugin.ui.dialog.SingleValueEditorDialog;
import com.baomidou.mybatisx.util.StringUtils;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;

public class DataTypeInfoPane extends SplitPane {

    TitledListPane<String> typeGroupListView;
    TitledListPane<DataType> dataTypeListView;

    public DataTypeInfoPane(DataTypeSystem typeSystem) {

        typeGroupListView = new TitledListPane<>("Type Group", typeSystem.getTypeGroupIds()) {
            @Override
            protected ListView<String> createListView(Collection<String> list) {
                ListView<String> listView = new ListView<>() {
                    @Override
                    public AnActionButtonRunnable getAddAction() {
                        return anActionButton -> {
                            SingleValueEditorDialog typeGroupEditDialog = new SingleValueEditorDialog("Please Input Type Group Identifier") {
                                @Override
                                public void onSubmit(String text) {
                                    typeGroupListView.getListView().addItem(text);
                                }
                            };
                            typeGroupEditDialog.show();
                        };
                    }

                };

                listView.setCellRenderer(new TextFieldListCellRenderer<>());
                // 添加鼠标监听器以处理双击事件
                listView.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) { // 检查是否为双击
                            int index = listView.locationToIndex(e.getPoint());
                            if (index != -1) {
                                String selectedValue = listView.getItem(index);

                            }
                        }
                    }
                });
                return listView;
            }
        };

        dataTypeListView = new TitledListPane<>("Data Type", Collections.emptyList()) {
            @Override
            protected ListView<DataType> createListView(Collection<DataType> list) {
                ListView<DataType> listView = new ListView<>() {
                    @Override
                    public AnActionButtonRunnable getAddAction() {
                        return new AnActionButtonRunnable() {
                            @Override
                            public void run(AnActionButton anActionButton) {
                                String typeGroup = typeGroupListView.getSelectedItem();
                                if (StringUtils.isEmpty(typeGroup)) {
                                    return;
                                }
                                DataTypeSaveOrUpdateDialog dialog = new DataTypeSaveOrUpdateDialog(typeGroup) {
                                    @Override
                                    protected void submit(@NotNull MultableDataType target, boolean saveOrUpdate) {
                                        dataTypeListView.getListView().addItem(target);
                                    }
                                };
                                dialog.show();
                            }
                        };
                    }
                };
                listView.setCellRenderer(new LabeledListCellRenderer<>() {
                    @Override
                    public String getLabelText(@NotNull DataType item, int index, boolean isSelected, boolean cellHasFocus) {
                        return item.getName();
                    }
                });
                return listView;
            }
        };

        // 添加选择监听器
        typeGroupListView.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // 确保选择事件已完成
                if (!e.getValueIsAdjusting()) {
                    String selectedValue = typeGroupListView.getSelectedItem();
                    if (StringUtils.hasText(selectedValue)) {
                        DataTypeSet types = typeSystem.getTypes(selectedValue);
                        if (types != null) {
                            dataTypeListView.setAll(types);
                        }
                    }
                }
            }
        });

        setComponent(typeGroupListView, dataTypeListView);
    }
}
