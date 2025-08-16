package com.baomidou.mybatisx.feat.jpa.common.type;


import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 排序区
 */
public class SuffixAppendType implements AppendType {
  @Override
  public String getName() {
    return AppendTypeEnum.SUFFIX.name();
  }

  /**
   * 允许所有字段
   *
   * @return
   */
  @Override
  public List<String> getAllowAfter() {
    return AppendTypeEnum.SUFFIX.getAllowedAfterList();
  }
}
