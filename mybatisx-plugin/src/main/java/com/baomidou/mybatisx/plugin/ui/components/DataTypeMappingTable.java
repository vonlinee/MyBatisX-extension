package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.component.ComboBoxCellEditor;
import com.baomidou.mybatisx.plugin.component.JBTableView;
import com.baomidou.mybatisx.util.ArrayUtils;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据类型映射表
 */
public class DataTypeMappingTable extends JBTableView<DataTypeMappingItem> {

    public DataTypeMappingTable() {
        super(new ListTableModel<>(new ColumnInfo[]{
            new ColumnInfo<DataTypeMappingItem, String>("Java") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getJavaType();
                }

                @Override
                public boolean isCellEditable(DataTypeMappingItem item) {
                    return true;
                }
            },

            new ColumnInfo<DataTypeMappingItem, String>("JDBC") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getJdbcType();
                }

                @Override
                public boolean isCellEditable(DataTypeMappingItem item) {
                    return true;
                }

                @Override
                public @NotNull TableCellEditor getEditor(DataTypeMappingItem item) {
                    ComboBoxCellEditor editor = new ComboBoxCellEditor(ArrayUtils.asList(JDBCType.values(), JDBCType::getName));
                    editor.addCellEditorListener(new CellEditorListener() {
                        @Override
                        public void editingStopped(ChangeEvent e) {
                            ComboBoxCellEditor eventCellEditor = (ComboBoxCellEditor) e.getSource();
                            JComboBox<?> comboBox = eventCellEditor.getComboBox();
                            Object selectedItem = comboBox.getSelectedItem();
                            item.setJdbcType(String.valueOf(selectedItem));
                        }

                        @Override
                        public void editingCanceled(ChangeEvent e) {

                        }
                    });
                    return editor;
                }
            },

            new ColumnInfo<DataTypeMappingItem, String>("MySQL") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getMysqlType();
                }

                @Override
                public boolean isCellEditable(DataTypeMappingItem item) {
                    return true;
                }
            },
            new ColumnInfo<DataTypeMappingItem, String>("Oracle") {

                @Override
                public @Nullable String valueOf(DataTypeMappingItem dataTypeItem) {
                    return dataTypeItem.getOracleType();
                }

                @Override
                public boolean isCellEditable(DataTypeMappingItem item) {
                    return true;
                }
            },
        }));

        ListTableModel<DataTypeMappingItem> model = getListTableModel();
        model.addRows(initDefaultDataTypeMappings());
    }

    public static List<DataTypeMappingItem> initDefaultDataTypeMappings() {

        List<DataTypeMappingItem> mappingItems = new ArrayList<>();
        mappingItems.add(newJavaTypeMappingPlaceHolder(boolean.class, JDBCType.BOOLEAN, "tinyint(1)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(byte.class, JDBCType.INTEGER, "int(5)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(char.class, JDBCType.CHAR, "char(10)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(short.class, JDBCType.INTEGER, "int(5)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(int.class, JDBCType.INTEGER, "int(5)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(float.class, JDBCType.DECIMAL, "decimal(5, 2)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(long.class, JDBCType.INTEGER, "int(5)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(double.class));

        mappingItems.add(newJavaTypeMappingPlaceHolder(Boolean.class, JDBCType.BOOLEAN, "tinyint(1)"));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Byte.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Character.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Short.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Integer.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Float.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Long.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Double.class));

        mappingItems.add(newJavaTypeMappingPlaceHolder(String.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(CharSequence.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(BigInteger.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(BigDecimal.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(LocalDate.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(LocalDateTime.class));
        mappingItems.add(newJavaTypeMappingPlaceHolder(Number.class));

        return mappingItems;
    }

    public static DataTypeMappingItem newJavaTypeMappingPlaceHolder(Class<?> simpleType, JDBCType jdbcType, String mysqlType) {
        DataTypeMappingItem typeMappingItem = new DataTypeMappingItem();
        typeMappingItem.setJavaType(simpleType.getName());
        typeMappingItem.setJdbcTypeEnum(jdbcType);
        typeMappingItem.setMysqlType(mysqlType);
        return typeMappingItem;
    }

    public static DataTypeMappingItem newJavaTypeMappingPlaceHolder(Class<?> simpleType) {
        DataTypeMappingItem typeMappingItem = new DataTypeMappingItem();
        typeMappingItem.setJavaType(simpleType.getName());
        typeMappingItem.setJdbcTypeEnum(JDBCType.VARCHAR);
        typeMappingItem.setMysqlType("varchar(50)");
        return typeMappingItem;
    }

    @Override
    protected AnActionButtonRunnable getAddAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                DataTypeMappingItem item = new DataTypeMappingItem();
                item.setJdbcTypeEnum(JDBCType.VARCHAR);
                addRow(item);

                int visibleRowCount = getVisibleRowCount();
                setEditingColumn(0);
                setEditingRow(visibleRowCount);
            }
        };
    }

    @Override
    protected AnActionButtonRunnable getRemoveAction() {
        return new AnActionButtonRunnable() {
            @Override
            public void run(AnActionButton anActionButton) {
                int selectedRow = getSelectedRow();
                remove(selectedRow);
            }
        };
    }
}
