package com.baomidou.plugin.idea.mybatisx.service;

import com.baomidou.plugin.idea.mybatisx.model.TranslationVO;

import java.util.List;

public interface Translation {

    /**
     * 翻译成中文
     *
     * @param content 需要翻译的文本
     * @return
     */
    List<TranslationVO> toChinese(String content);
}
