package com.baomidou.mybatisx.plugin.intention

import lombok.Getter

@Getter
enum class ImportModel(val type: Int, val label: String) {
  OVERRIDE(1, "全部覆盖"),
  APPEND(2, "仅追加"),
  MERGE(3, "合并不覆盖"),
  MERGE_OVERRIDE(4, "合并且覆盖");

  @Override
  override fun toString(): String {
    return label;
  }
}
