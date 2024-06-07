package com.baomidou.plugin.idea.mybatisx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BaiduTranslationResponse {

    @JsonProperty("error_code")
    private String errorCode;
    @JsonProperty("error_msg")
    private String errorMsg;
    private String from;
    private String to;

    @JsonProperty("trans_result")
    private List<TransResult> transResult;
}
