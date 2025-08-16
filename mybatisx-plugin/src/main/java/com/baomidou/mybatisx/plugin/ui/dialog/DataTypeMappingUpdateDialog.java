package com.baomidou.mybatisx.plugin.ui.dialog;

import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.plugin.components.HBox;
import com.baomidou.mybatisx.plugin.components.SimpleComboBox;
import com.baomidou.mybatisx.plugin.setting.DataTypeSettings;
import com.baomidou.mybatisx.plugin.ui.components.DataTypeMappingItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DataTypeMappingUpdateDialog extends DialogBase {

  String typeGroup;
  String anotherTypeGroup;
  private SimpleComboBox<String> types = new SimpleComboBox<>();
  private SimpleComboBox<String> anotherTypes = new SimpleComboBox<>();

  public DataTypeMappingUpdateDialog(@NotNull String typeGroup, @NotNull String anotherTypeGroup) {
    this.typeGroup = typeGroup;
    this.anotherTypeGroup = anotherTypeGroup;
    DataTypeSettings dataTypeSettings = DataTypeSettings.getInstance();
    DataTypeSystem typeSystem = dataTypeSettings.getState();
    types.addItems(typeSystem.getTypeIdentifiers(typeGroup));
    anotherTypes.addItems(typeSystem.getTypeIdentifiers(anotherTypeGroup));
    setOKActionEnabled(true);
    setTitle("Add DataType Mapping: " + typeGroup + " -> " + anotherTypeGroup);
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    HBox hBox = new HBox();
    hBox.addChildren(types, anotherTypes);
    return hBox;
  }

  public DataTypeMappingItem getTypeMappingItem() {
    DataTypeMappingItem item = new DataTypeMappingItem();
    item.setGroup(typeGroup);
    item.setAnotherGroup(anotherTypeGroup);
    item.setIdentifier(types.getSelectedItem());
    item.setAnotherIdentifier(anotherTypes.getSelectedItem());
    return item;
  }
}
