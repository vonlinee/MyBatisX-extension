package com.baomidou.mybatisx.service;

import com.baomidou.mybatisx.model.BaiduTranslationResponse;
import com.baomidou.mybatisx.model.TranslationResult;
import com.baomidou.mybatisx.model.TranslationVO;
import com.baomidou.mybatisx.util.MD5;
import com.baomidou.mybatisx.util.RestTemplateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaiduTranslationService implements Translation {

    /**
     * 通用翻译
     */
    private final static String UNIVERSAL_API = "http://fanyi-api.baidu.com/api/trans/vip/translate";

    private final String appId;

    private final String secret;

    private final static String CHINESE = "zh";

    public BaiduTranslationService(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    /**
     * 翻译成中文
     *
     * @param content 需要翻译的文本
     * @return
     */
    @Override
    public List<TranslationVO> toChinese(String content) {
        MultiValueMap<String, String> request = buildParameter(content, CHINESE,
            this.appId, this.secret);
        String response = new RestTemplateUtil().post(UNIVERSAL_API, new HashMap<>(), request, MediaType.APPLICATION_FORM_URLENCODED);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<TranslationVO> resultList = new ArrayList<>();
            BaiduTranslationResponse baiduTranslationResponse = objectMapper.readValue(response, BaiduTranslationResponse.class);
            if (null == baiduTranslationResponse) {
                return new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(baiduTranslationResponse.getTranslationResult())) {
                List<TranslationResult> translationResult = baiduTranslationResponse.getTranslationResult();
                for (TranslationResult result : translationResult) {
                    resultList.add(new TranslationVO(result.getSrc(), result.getDst()));
                }
                return resultList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 构建请求参数
     *
     * @param query  需要翻译的文本
     * @param to     需要翻译的语种
     * @param appId  appid <a href="http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer">...</a>
     * @param secret secret <a href="http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer">...</a>
     * @return
     */
    private MultiValueMap<String, String> buildParameter(String query, String to, String appId, String secret) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        String random = String.valueOf(System.currentTimeMillis());
        request.add("q", query);
        request.add("from", "auto");
        request.add("to", to);
        request.add("appid", appId);
        request.add("salt", random);
        request.add("sign", buildSign(random, appId, secret, query));
        return request;
    }

    private String buildSign(String random, String appId, String secret, String query) {
        return MD5.md5(appId + query + random + secret);
    }
}
