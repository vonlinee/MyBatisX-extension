package com.baomidou.mybatisx.feat.jpa.common.command;


import com.baomidou.mybatisx.feat.jpa.common.SyntaxAppender;
import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;

import java.util.Optional;

/**
 * The type Field suffix append type service.
 */
public class FieldSuffixAppendTypeService implements AppendTypeCommand {

    private final SyntaxAppender syntaxAppender;

    /**
     * Instantiates a new Field suffix append type service.
     *
     * @param syntaxAppender the syntax appender
     */
    public FieldSuffixAppendTypeService(final SyntaxAppender syntaxAppender) {

        this.syntaxAppender = syntaxAppender;
    }

    @Override
    public Optional<SyntaxAppender> execute() {
        if (this.syntaxAppender.getType() == AppendTypeEnum.SUFFIX) {
            return Optional.of(this.syntaxAppender);
        }
        return Optional.empty();
    }
}
