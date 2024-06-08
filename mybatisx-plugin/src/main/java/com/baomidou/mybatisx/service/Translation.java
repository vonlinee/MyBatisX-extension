package com.baomidou.mybatisx.service;

import com.baomidou.mybatisx.model.TranslationVO;

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
