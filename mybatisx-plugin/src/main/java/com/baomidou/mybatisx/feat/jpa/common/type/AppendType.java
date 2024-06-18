package com.baomidou.mybatisx.feat.jpa.common.type;

import java.util.List;

/**
 * The interface Append type.
 */
public interface AppendType {

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets allow after.
     *
     * @return to allow after
     */
    List<String> getAllowAfter();

    /**
     * Check after boolean.
     *
     * @param appendType the append type
     * @return the boolean
     */
    default boolean checkAfter(AppendType appendType) {
        return getAllowAfter().contains(appendType.getName());
    }
}
