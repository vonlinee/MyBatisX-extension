package com.baomidou.mybatisx.agent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class VMContext {

    private static final Map<String, VMInfo> MACHINE_MAP = new ConcurrentHashMap<>();

    public static void put(String key, VMInfo virtualMachine) {
        MACHINE_MAP.put(key, virtualMachine);
    }

    public static VMInfo get(String key) {
        return MACHINE_MAP.get(key);
    }

    public static int size() {
        return MACHINE_MAP.size();
    }

    public static void remove(String key) {
        MACHINE_MAP.remove(key);
    }

    public static Collection<VMInfo> values() {
        return Collections.unmodifiableCollection(MACHINE_MAP.values());
    }

    public static Optional<VMInfo> first() {
        return MACHINE_MAP.values().stream().findFirst();
    }
}
