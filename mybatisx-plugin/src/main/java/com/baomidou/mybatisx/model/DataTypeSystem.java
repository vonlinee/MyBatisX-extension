package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.plugin.setting.configurable.ConfigurableObject;
import com.baomidou.mybatisx.plugin.ui.components.DataType;
import com.baomidou.mybatisx.plugin.ui.components.DataTypeItem;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.JDBCType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class DataTypeSystem extends ConfigurableObject {

    @NotNull
    Map<String, GroupedDataTypeSet> dataTypeMap;
    @NotNull
    @Getter
    DataTypeMappingSystem typeMapping;

    public DataTypeSystem() {
        this(new HashMap<>(), new DataTypeMappingSystem());
    }

    DataTypeSystem(@NotNull Map<String, GroupedDataTypeSet> dataTypeMap, @NotNull DataTypeMappingSystem typeMapping) {
        this.dataTypeMap = dataTypeMap;
        this.typeMapping = typeMapping;
    }

    public Set<String> getTypeGroupIds() {
        return dataTypeMap.keySet();
    }

    /**
     * 添加数据类型
     *
     * @param groupId      类型分组
     * @param dataTypeItem 数据类型
     * @return 是否成功
     */
    public boolean addDataType(String groupId, DataType dataTypeItem) {
        if (dataTypeMap.containsKey(groupId)) {
            GroupedDataTypeSet typeSet = dataTypeMap.get(groupId);
            if (typeSet != null) {
                return typeSet.add(dataTypeItem);
            }
            typeSet = new GroupedDataTypeSet(groupId);
            return typeSet.add(dataTypeItem);
        } else {
            GroupedDataTypeSet typeSet = new GroupedDataTypeSet(groupId);
            typeSet.add(dataTypeItem);
            dataTypeMap.put(groupId, typeSet);
            return true;
        }
    }

    public void addDataTypes(Collection<? extends DataType> dataTypes) {
        dataTypes.forEach(dataType -> addDataType(dataType.getGroupIdentifier(), dataType));
    }

    public DataTypeSet getTypes(@NotNull String groupId) {
        return dataTypeMap.get(groupId);
    }

    public Set<String> getTypeIdentifiers(@NotNull String groupId) {
        GroupedDataTypeSet typeSet = dataTypeMap.get(groupId);
        return typeSet.stream().map(DataType::getIdentifier).collect(Collectors.toSet());
    }

    public DataType getType(String group, String typeId) {
        GroupedDataTypeSet typeSet = dataTypeMap.get(group);
        if (typeSet == null) {
            return null;
        }
        for (DataType dataType : typeSet) {
            if (Objects.equals(dataType.getIdentifier(), typeId)) {
                return dataType;
            }
        }
        return null;
    }

    public void initBuiltinTypeSystem() {
        // JDBC
        String group = "JDBC";
        GroupedDataTypeSet jdbcTypeSet = new GroupedDataTypeSet(group);
        for (JDBCType value : JDBCType.values()) {
            jdbcTypeSet.add(new DataTypeItem(group, value.getName()));
        }
        dataTypeMap.put(group, jdbcTypeSet);

        // Java
        group = "Java";
        GroupedDataTypeSet javaTypeSet = new GroupedDataTypeSet(group);
        javaTypeSet.add(new DataTypeItem(group, "byte"));
        javaTypeSet.add(new DataTypeItem(group, "short"));
        javaTypeSet.add(new DataTypeItem(group, "char"));
        javaTypeSet.add(new DataTypeItem(group, "int"));
        javaTypeSet.add(new DataTypeItem(group, "long"));
        javaTypeSet.add(new DataTypeItem(group, "float"));
        javaTypeSet.add(new DataTypeItem(group, "double"));
        javaTypeSet.add(new DataTypeItem(group, "boolean"));
        dataTypeMap.put(group, javaTypeSet);

        // initialize type mapping
        this.typeMapping.initInternalMapping(this);
    }

    public boolean isEmpty() {
        return dataTypeMap.isEmpty();
    }

    public boolean addTypeGroup(String typeGroup) {
        if (typeGroup.matches("^[A-Za-z][A-Za-z0-9]*$")) {
            if (dataTypeMap.containsKey(typeGroup)) {
                return false;
            }
            dataTypeMap.put(typeGroup, new GroupedDataTypeSet(typeGroup));
            return true;
        }
        return false;
    }

    public GroupedDataTypeSet removeTypeGroup(String typeGroup) {
        return dataTypeMap.remove(typeGroup);
    }

    @NotNull
    public DataTypeSystem copy() {
        return new DataTypeSystem(new HashMap<>(dataTypeMap), typeMapping.copy());
    }
}
