package com.baomidou.mybatisx.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslationVO {

  private String src;

  private String dst;

  public TranslationVO(String src, String dst) {
    this.src = src;
    this.dst = dst;
  }
}
