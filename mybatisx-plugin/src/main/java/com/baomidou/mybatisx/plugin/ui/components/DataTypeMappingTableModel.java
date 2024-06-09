package com.baomidou.mybatisx.plugin.ui.components;

import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据类型映射表
 */
public class DataTypeMappingTableModel extends DefaultTableModel {

    public DataTypeMappingTableModel() {
        super(null, new String[]{"分组", "主类型", "映射类型"});
    }

    public static List<Object[]> readRows(URI input) {
        Config config = new Config();
        // 设置Section不允许出现重复
        config.setMultiSection(false);
        Ini ini = new Ini();
        ini.setConfig(config);
        List<Object[]> rows = new ArrayList<>();
        try {
            // 加载配置文件
            ini.load(input.toURL());
            Set<Map.Entry<String, Profile.Section>> sectionSet = ini.entrySet();
            for (Map.Entry<String, Profile.Section> entry : sectionSet) {
                String sectionName = entry.getKey();
                Profile.Section section = entry.getValue();
                for (Map.Entry<String, String> kvEntry : section.entrySet()) {
                    rows.add(new Object[]{sectionName, kvEntry.getKey(), kvEntry.getValue()});
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
        List<Object[]> rows = readRows(uri);
        for (Object[] row : rows) {
            this.addRow(row);
        }
    }
}
