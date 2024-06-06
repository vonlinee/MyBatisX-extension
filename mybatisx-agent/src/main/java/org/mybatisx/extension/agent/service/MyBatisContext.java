package org.mybatisx.extension.agent.service;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatisx.extension.agent.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public final class MyBatisContext {

    private static final
    List<String> targetFields = Arrays.asList("mappedStatements", "caches", "resultMaps", "parameterMaps", "keyGenerators", "sqlFragments");
    static boolean initialized = false;

    static Method REPLACE_METHOD = null;

    private MyBatisContext() {
    }

    public static final List<Configuration> configurations = new ArrayList<>();

    public static void addConfiguration(Configuration configuration) {
        configurations.add(configuration);
    }

    public static List<Configuration> getConfigurations() {
        return configurations;
    }

    /**
     * 暂时认为一个项目只有一个Configuration实例
     *
     * @return Configuration实例
     */
    public static Configuration getConfiguration() {
        if (!configurations.isEmpty()) {
            return configurations.get(0);
        }
        return null;
    }

    public static void init(String methodName, Object configuration) {
        if (!initialized) {
            try {
                Method method = configuration.getClass().getDeclaredMethod(methodName, String.class, MappedStatement.class);
                method.setAccessible(true);
                REPLACE_METHOD = method;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                swapStrictMap((Configuration) configuration);
            } catch (Throwable throwable) {
                Log.error("failed to swap StrictMap in Configuration", throwable);
            }
            initialized = true;
        }
    }

    public static void replace(String id, MappedStatement mappedStatement) {
        for (Configuration configuration : configurations) {
            Object[] params = {id, mappedStatement};
            try {
                REPLACE_METHOD.invoke(configuration, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换掉mybatis默认的strictMap，把put方法中的containsKey异常校验去掉
     *
     * @param configuration MyBatis 配置类
     * @throws IllegalStateException 替换strictMap失败
     *                               Configuration.StrictMap
     */
    public static void swapStrictMap(Configuration configuration) {
        Class<? extends Configuration> configurationClass = configuration.getClass();
        Field[] declaredFields = configurationClass.getDeclaredFields();
        Map<String, Field> fieldMap = Arrays.stream(declaredFields).collect(Collectors.toMap(Field::getName, obj -> obj));
        targetFields.stream().filter(fieldMap::containsKey).forEach(f -> {
            Field field = fieldMap.get(f);
            try {
                field.setAccessible(true);
                // map集合对象复制
                Class<?> originStrictMapClass = field.get(configuration).getClass();
                @SuppressWarnings("unchecked")
                Set<Map.Entry<?, ?>> entrySet = (Set<Map.Entry<?, ?>>) originStrictMapClass.getMethod("entrySet").invoke(field.get(configuration));
                MyBatisMapperXmlHotSwapAgentHandler.StrictMap<Object> strictMap = new MyBatisMapperXmlHotSwapAgentHandler.StrictMap<>(f + " collection");
                entrySet.forEach(s -> strictMap.put(String.valueOf(s.getKey()), s.getValue()));
                field.set(configuration, strictMap);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new IllegalStateException("failed to hotswap mapper xml file: :" + e.getMessage(), e);
            }
        });
    }
}
