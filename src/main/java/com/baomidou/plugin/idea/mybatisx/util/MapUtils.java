package com.baomidou.plugin.idea.mybatisx.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapUtils {

    /**
     * @param data key扁平化的map，例如 {"user.name": "zs", "user.age" : 26, "sex": false}
     * @return 嵌套Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> expandKeys(Map<String, Object> data, String separator) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String[] keys = entry.getKey().split("\\.");
            Object value = entry.getValue();
            Map<String, Object> map = result;
            for (int i = 0; i < keys.length - 1; i++) {
                String key = keys[i];
                if (!map.containsKey(key)) {
                    map.put(key, new LinkedHashMap<>());
                }
                map = (Map<String, Object>) map.get(key);
            }
            map.put(keys[keys.length - 1], value);
        }
        return result;
    }
}
