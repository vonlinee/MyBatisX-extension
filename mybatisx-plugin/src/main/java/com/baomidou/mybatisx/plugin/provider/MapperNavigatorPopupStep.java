package com.baomidou.mybatisx.plugin.provider;

import com.intellij.openapi.ui.popup.PopupStep;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class MapperNavigatorPopupStep extends MapperStatementContextMenuListPopupStep {

  public MapperNavigatorPopupStep(MapperStatementItem item, List<MapperNavigator> navigators) {
    super(item, navigators);
  }

  @Override
  public Icon getIconFor(MapperNavigator navigator) {
    return navigator.getIcon();
  }

  @Override
  public @NotNull String getTextFor(MapperNavigator navigator) {
    return navigator.getDisplayText();
  }

  @Override
  public PopupStep<?> onChosen(MapperNavigator navigator, boolean finalChoice) {
    navigator.navigate(this.myItem.getProject(), myItem);
    return PopupStep.FINAL_CHOICE;
  }
}
