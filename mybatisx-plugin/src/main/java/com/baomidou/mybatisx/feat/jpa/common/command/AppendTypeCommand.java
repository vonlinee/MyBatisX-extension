package com.baomidou.mybatisx.feat.jpa.common.command;


import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;

import java.util.Optional;

/**
 * The interface Append type command.
 */
public interface AppendTypeCommand {
    /**
     * Execute optional.
     *
     * @return the optional
     */
    Optional<SyntaxAppender> execute();
}
