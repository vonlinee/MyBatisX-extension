package com.baomidou.mybatisx.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslationResult {

    /**
     * 源文本
     */
    private String src;

    /**
     * 翻译后的文本
     */
    private String dst;
}
