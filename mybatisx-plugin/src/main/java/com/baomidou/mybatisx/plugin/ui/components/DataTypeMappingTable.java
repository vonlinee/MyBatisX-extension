package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.JBTableView;
import com.baomidou.mybatisx.util.IdeSDK;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类型映射表
 */
public class DataTypeMappingTable extends JBTableView<DataTypeMappingItem> {

    public DataTypeMappingTable() {

        ColumnInfo<DataTypeMappingItem, String> col1 = new ColumnInfo<DataTypeMappingItem, String>("类型分组") {

            @Override
            public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                return dataTypeItem.getGroup();
            }
        };

        ColumnInfo<DataTypeMappingItem, String> col2 = new ColumnInfo<DataTypeMappingItem, String>("类型名称") {

            @Override
            public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                return dataTypeItem.getIdentifier();
            }
        };

        ColumnInfo<DataTypeMappingItem, String> col3 = new ColumnInfo<DataTypeMappingItem, String>("映射类型分组") {

            @Override
            public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                return dataTypeItem.getAnotherGroup();
            }
        };

        ColumnInfo<DataTypeMappingItem, String> col4 = new ColumnInfo<DataTypeMappingItem, String>("映射类型名称") {

            @Override
            public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                return dataTypeItem.getAnotherIdentifier();
            }
        };

        ListTableModel<DataTypeMappingItem> model = new ListTableModel<>(col1, col2, col3, col4);

        this.setModelAndUpdateColumns(model);
    }

    @Override
    protected void initToolbarDecoratorExtra(ToolbarDecorator decorator) {
        decorator.addExtraAction(new AnActionButton() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                IdeSDK.chooseSingleFile(null, vf -> load(new File(vf.getPath()).toURI()));
            }
        });
    }

    public static List<DataTypeMappingItem> readRows(URI input) {
        Config config = new Config();
        // 设置Section不允许出现重复
        config.setMultiSection(false);
        Ini ini = new Ini();
        ini.setConfig(config);
        List<DataTypeMappingItem> rows = new ArrayList<>();
        try {
            // 加载配置文件
            ini.load(input.toURL());
            Set<Map.Entry<String, Profile.Section>> sectionSet = ini.entrySet();
            for (Map.Entry<String, Profile.Section> entry : sectionSet) {
                String sectionName = entry.getKey();
                Profile.Section section = entry.getValue();
                for (Map.Entry<String, String> kvEntry : section.entrySet()) {
                    DataTypeMappingItem item = new DataTypeMappingItem();
                    item.setGroup(sectionName);
                    item.setIdentifier(kvEntry.getKey());
                    item.setAnotherIdentifier(kvEntry.getValue());
                    rows.add(item);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 加载 ini 配置文件
     */
    public void load(URI uri) {
        List<DataTypeMappingItem> rows = readRows(uri);
        for (DataTypeMappingItem row : rows) {
            this.addRow(row);
        }
    }

    @Override
    protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {

            }
        };
    }
}
