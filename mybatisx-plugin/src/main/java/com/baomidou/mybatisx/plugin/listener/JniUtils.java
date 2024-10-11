package com.baomidou.mybatisx.plugin.listener;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class JniUtils {

    public static void getLoadedNativeLibs(ClassLoader cl) throws RuntimeException {
        try {
            Field field = ClassLoader.class.getDeclaredField("nativeLibraries");
            field.setAccessible(true);
            Object nativeLibraries = field.get(cl);
            if (nativeLibraries instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nativeLibrariesMap = (Map<String, Object>) nativeLibraries;
                System.out.println(nativeLibrariesMap);
            }
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
