package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.plugin.ui.components.DataType;
import com.baomidou.mybatisx.plugin.ui.components.DataTypeMappingItem;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * key:
 * a->b: {
 * a1 -> [b1, b2, b3, ... bn],
 * a2 -> [b1, b2, b3, ... bn]
 * }
 */
@Data
public class DataTypeMappingSystem {

  Map<String, Map<String, Set<String>>> mappings = new HashMap<>();
  private Map<String, String> groupMapping = new HashMap<>();

  public static String getTypeGroupMappingKey(String typeGroup, String anotherTypeGroup) {
    return typeGroup + ">" + anotherTypeGroup;
  }

  public void addMapping(DataType type, DataType anotherType) {
    if (Objects.equals(type.getGroupIdentifier(), anotherType.getGroupIdentifier())) {
      return;
    }
    String key = type.getGroupIdentifier() + "->" + anotherType.getGroupIdentifier();
    if (mappings.containsKey(key)) {
      Map<String, Set<String>> map = mappings.get(key);
      if (map == null) {
        map = new HashMap<>();
        Set<String> types = map.putIfAbsent(type.getIdentifier(), new HashSet<>());
        if (types != null) {
          types.add(anotherType.getIdentifier());
        }
      } else {
        Set<String> types = map.putIfAbsent(type.getIdentifier(), new HashSet<>());
        if (types != null) {
          types.add(anotherType.getIdentifier());
        }
      }
    } else {
      HashMap<String, Set<String>> map = new HashMap<>();
      Set<String> types = map.putIfAbsent(type.getIdentifier(), new HashSet<>());
      if (types != null) {
        types.add(anotherType.getIdentifier());
      }
      mappings.put(key, map);
    }
  }

  public void initInternalMapping(DataTypeSystem typeSystem) {

  }

  public boolean addTypeMappingItem(DataTypeMappingItem item) {
    if (Objects.equals(item.getGroup(), item.getAnotherGroup())) {
      return false;
    }
    final String key = getTypeGroupMappingKey(item.getGroup(), item.getAnotherGroup());
    if (mappings.containsKey(key)) {
      Map<String, Set<String>> map = mappings.get(key);
      if (map == null) {
        map = new HashMap<>();
        Set<String> types = new HashSet<>();
        types.add(item.getAnotherIdentifier());
        map.put(item.getIdentifier(), types);
      } else {
        Set<String> types = map.putIfAbsent(item.getIdentifier(), new HashSet<>());
        if (types != null) {
          return types.add(item.getAnotherIdentifier());
        }
      }
    } else {
      HashMap<String, Set<String>> map = new HashMap<>();
      Set<String> types = new HashSet<>();
      types.add(item.getAnotherIdentifier());
      map.put(item.getIdentifier(), types);
      mappings.put(key, map);
    }
    return true;
  }

  @Nullable
  public Map<String, Set<String>> getTypeMapping(String typeGroup, String anotherTypeGroup) {
    return mappings.get(getTypeGroupMappingKey(typeGroup, anotherTypeGroup));
  }

  public boolean removeTypeMapping(DataTypeMappingItem item) {
    if (Objects.equals(item.getGroup(), item.getAnotherGroup())) {
      return false;
    }
    final String key = getTypeGroupMappingKey(item.getGroup(), item.getAnotherGroup());
    if (mappings.containsKey(key)) {
      Map<String, Set<String>> map = mappings.get(key);
      if (map == null) {
        return false;
      }
      Set<String> types = map.get(item.getIdentifier());
      if (types == null) {
        return false;
      }
      return types.remove(item.getAnotherIdentifier());
    }
    return false;
  }

  @NotNull
  public DataTypeMappingSystem copy() {
    DataTypeMappingSystem dataTypeMappingSystem = new DataTypeMappingSystem();
    dataTypeMappingSystem.groupMapping = new HashMap<>(groupMapping);
    dataTypeMappingSystem.mappings = new HashMap<>(mappings);
    return dataTypeMappingSystem;
  }
}
