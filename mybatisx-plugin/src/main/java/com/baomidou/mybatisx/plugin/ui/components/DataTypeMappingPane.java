package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.DataTypeMappingSystem;
import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.component.SimpleComboBox;
import com.baomidou.mybatisx.util.CollectionUtils;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataTypeMappingPane extends BorderPane {

  HBox hBox = new HBox();
  SimpleComboBox<String> type = new SimpleComboBox<>();
  SimpleComboBox<String> anotherType = new SimpleComboBox<>();
  DataTypeMappingConfigTable dataTypeMappingConfigTable;
  DataTypeMappingSystem typeMapping;

  public DataTypeMappingPane(DataTypeSystem typeSystem) {
    typeMapping = typeSystem.getTypeMapping();
    dataTypeMappingConfigTable = new DataTypeMappingConfigTable(typeMapping);

    hBox.addChildren(type, anotherType);

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

    fireTypeSystemChange(typeSystem);

    addToTop(hBox);
    addToCenter(dataTypeMappingConfigTable);
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

    dataTypeMappingConfigTable.setGroupMapping(selectedItem, anotherType.getSelectedItem());
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
    dataTypeMappingConfigTable.getTableViewModel().removeAllRows();
    dataTypeMappingConfigTable.addRows(mappingItems);
  }

  public void fireTypeSystemChange(DataTypeSystem typeSystem) {
    if (type.getItems().isEmpty()) {
      type.addItems(typeSystem.getTypeGroupIds());
    } else {
      type.removeAllItems();
      type.addItems(typeSystem.getTypeGroupIds());
    }
    type.setSelectedIndex(0);
    firePrimaryTypeGroupChange(type.getSelectedItem());
  }

  public void addTypeGroup(String newTypeGroup) {
    if (!type.getItems().contains(newTypeGroup)) {
      type.addItem(newTypeGroup);
    }
  }

  public void removeTypeGroup(String typeGroup) {
    type.removeItem(typeGroup);
  }

  public boolean isModified() {
    return dataTypeMappingConfigTable.isModified();
  }
}
