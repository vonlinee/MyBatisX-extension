package com.baomidou.mybatisx.util;

import net.minidev.json.JSONStyle;
import net.minidev.json.reader.JsonWriter;
import net.minidev.json.reader.JsonWriterI;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawuse"})
public abstract class JsonUtils {

    public static String toJSONString(Object obj) {
        if (obj == null) {
            return "{}";
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        StringBuilder res = new StringBuilder();
        JsonWriterI writer;
        if (obj instanceof Map) {
            writer = JsonWriter.JSONMapWriter;
        } else if (obj.getClass().isArray()) {
            writer = JsonWriter.arrayWriter;
        } else {
            writer = JsonWriter.beansWriter;
        }
        try {
            writer.writeJSONString(obj, res, JSONStyle.NO_COMPRESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

}
