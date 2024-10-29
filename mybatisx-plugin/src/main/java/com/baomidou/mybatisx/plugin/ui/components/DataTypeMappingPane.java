package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.DataTypeMappingSystem;
import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.baomidou.mybatisx.plugin.component.Button;
import com.baomidou.mybatisx.plugin.component.DefaultListTableModel;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.component.SimpleComboBox;
import com.baomidou.mybatisx.plugin.ui.dialog.DataTypeMappingUpdateDialog;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataTypeMappingPane extends BorderPane {

    HBox hBox = new HBox();

    SimpleComboBox<String> type = new SimpleComboBox<>();
    SimpleComboBox<String> anotherType = new SimpleComboBox<>();
    DataTypeMappingTable dataTypeMappingTable;
    DataTypeMappingSystem typeMapping;

    public DataTypeMappingPane(DataTypeSystem typeSystem) {
        dataTypeMappingTable = new DataTypeMappingTable();
        typeMapping = typeSystem.getTypeMapping();

        hBox.addChildren(type, anotherType);

        Button btnAdd = new Button("Add");
        btnAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (StringUtils.hasText(type.getItem(), anotherType.getItem())) {
                    DataTypeMappingUpdateDialog dialog = new DataTypeMappingUpdateDialog(type.getItem(), anotherType.getItem()) {
                        @Override
                        protected @NotNull Action getOKAction() {
                            return new DialogWrapperAction("Add Mapping") {
                                @Override
                                protected void doAction(ActionEvent e) {
                                    DataTypeMappingItem typeMappingItem = getTypeMappingItem();
                                    if (typeMapping.addTypeMappingItem(typeMappingItem)) {
                                        dataTypeMappingTable.addRow(typeMappingItem);
                                    }
                                }
                            };
                        }
                    };
                    dialog.show();
                }
            }
        });

        Button btnRemove = new Button("Remove");
        btnRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = dataTypeMappingTable.getSelectedRow();
                DefaultListTableModel<DataTypeMappingItem> model = dataTypeMappingTable.getModel();
                DataTypeMappingItem item = model.getItem(selectedRow);
                typeMapping.removeTypeMapping(item);
                model.removeRow(selectedRow);
            }
        });

        hBox.add(btnAdd);
        hBox.add(btnRemove);

        type.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 获取选中的元素
                    String selectedItem = (String) e.getItem();
                    firePrimaryTypeGroupChange(selectedItem);
                    changeDataTypeMappings();
                }
            }
        });

        type.addItems(typeSystem.getTypeGroupIds());
        type.setSelectedIndex(0);
        firePrimaryTypeGroupChange(type.getSelectedItem());

        addToTop(hBox);
        addToCenter(dataTypeMappingTable);
    }

    private void firePrimaryTypeGroupChange(String selectedItem) {
        List<String> items = CollectionUtils.modifiableList(type.getItems());
        Object selectedAnotherType = anotherType.getSelectedItem();
        items.remove(selectedItem);
        anotherType.setItems(items);
        if (items.contains(selectedItem)) {
            anotherType.setSelectedItem(selectedAnotherType);
        } else if (!items.isEmpty()) {
            anotherType.setSelectedIndex(0);
        }
    }

    private void changeDataTypeMappings() {
        String typeSelectedItem = type.getSelectedItem();
        String anotherTypeSelectedItem = anotherType.getSelectedItem();
        Map<String, Set<String>> mappings = typeMapping.getTypeMapping(typeSelectedItem, anotherTypeSelectedItem);
        if (mappings == null || mappings.isEmpty()) {
            return;
        }
        List<DataTypeMappingItem> mappingItems = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : mappings.entrySet()) {
            Set<String> list = entry.getValue();
            if (CollectionUtils.isNotEmpty(list)) {
                for (String anotherType : list) {
                    DataTypeMappingItem item = new DataTypeMappingItem();
                    item.setIdentifier(entry.getKey());
                    item.setAnotherGroup(anotherTypeSelectedItem);
                    item.setAnotherIdentifier(anotherType);
                    mappingItems.add(item);
                }
            }
        }
        dataTypeMappingTable.getModel().removeAllRows();
        dataTypeMappingTable.addRows(mappingItems);
    }
}
