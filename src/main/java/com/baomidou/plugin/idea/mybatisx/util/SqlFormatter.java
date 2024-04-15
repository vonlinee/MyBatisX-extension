package com.baomidou.plugin.idea.mybatisx.util;

/**
 * Formatter contract
 */
public interface SqlFormatter {
    /**
     * Format the source SQL string.
     *
     * @param source The original SQL string
     * @return The formatted version
     */
    String format(String source);
}
