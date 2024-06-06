package com.baomidou.plugin.idea.mybatisx.jpa.common.appender.changer;

import org.jetbrains.annotations.NotNull;

/**
 * The type Not in parameter changer.
 */
public class NotInParameterChanger extends InParameterChanger {
    @Override
    protected @NotNull
    String getIn() {
        return "not in";
    }
}
