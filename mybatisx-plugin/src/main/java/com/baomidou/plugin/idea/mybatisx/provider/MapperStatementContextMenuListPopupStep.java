package com.baomidou.plugin.idea.mybatisx.provider;

import com.intellij.openapi.ui.popup.util.BaseListPopupStep;

import java.util.List;

abstract class MapperStatementContextMenuListPopupStep extends BaseListPopupStep<MapperNavigator> {

    protected MapperStatementItem myItem;

    public MapperStatementContextMenuListPopupStep(MapperStatementItem item, List<MapperNavigator> navigators) {
        super(null, navigators);
        this.myItem = item;
    }
}
