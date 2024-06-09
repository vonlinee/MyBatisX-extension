package com.baomidou.mybatisx.plugin.ui;

/**
 * The interface Executable listener.
 *
 * @author yanglin
 */
public interface ExecutableListener {

    /**
     * Is write action boolean.
     *
     * @return the boolean
     */
    default boolean isWriteAction() {
        return false;
    }
}
