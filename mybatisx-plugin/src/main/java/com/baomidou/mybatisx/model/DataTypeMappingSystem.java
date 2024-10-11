package com.baomidou.mybatisx.model;

import com.baomidou.mybatisx.plugin.ui.components.DataType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * key:
 * a->b: {
 * a1 -> [b1, b2, b3, ... bn],
 * a2 -> [b1, b2, b3, ... bn]
 * }
 */
@Data
public class DataTypeMappingSystem {

    private Map<String, String> groupMapping = new HashMap<>();

    Map<String, Map<String, String>> mappings = new HashMap<>();

    public void addMapping(DataType type, DataType anotherType) {
        if (Objects.equals(type.getGroupIdentifier(), anotherType.getGroupIdentifier())) {
            return;
        }

        String key = type.getGroupIdentifier() + "->" + anotherType.getGroupIdentifier();
        if (mappings.containsKey(key)) {
            Map<String, String> map = mappings.get(key);
            if (map == null) {
                map = new HashMap<>();
                map.put(type.getIdentifier(), anotherType.getIdentifier());
            } else {
                map.put(type.getIdentifier(), anotherType.getIdentifier());
            }
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(type.getIdentifier(), anotherType.getIdentifier());
            mappings.put(key, map);
        }
    }

    public void initInternalMapping(DataTypeSystem typeSystem) {

    }
}
