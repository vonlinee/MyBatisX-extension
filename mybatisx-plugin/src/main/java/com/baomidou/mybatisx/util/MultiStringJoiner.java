package com.baomidou.mybatisx.util;

public class MultiStringJoiner {

  private final CharSequence prefix;
  private final CharSequence suffix;
  private final String delimiter;
  private final String newLine;
  private final int step;
  private final StringBuilder stringBuilder = new StringBuilder();
  int currentIndex = 0;

  /**
   * @param delimiter 分隔符
   * @param newLine   换行标识
   * @param step      换行步长
   */
  public MultiStringJoiner(String delimiter, CharSequence prefix, CharSequence suffix, String newLine, int step) {
    this.delimiter = delimiter;
    this.prefix = prefix;
    this.suffix = suffix;
    this.newLine = newLine;
    this.step = step;
  }

  public MultiStringJoiner add(CharSequence str) {
    stringBuilder.append(str).append(delimiter);
    currentIndex++;
    if (currentIndex % step == 0) {
      stringBuilder.append(newLine);
    }
    return this;
  }

  @Override
  public String toString() {
    if (currentIndex == 0) {
      return "";
    }
    final int lastDelimiterIndex = stringBuilder.lastIndexOf(delimiter);
    return prefix + stringBuilder.substring(0, lastDelimiterIndex) + suffix;
  }


  /**
   * 目前没有用合并的场景, 所以这里就不实现了
   *
   * @param str MultiStringJoiner
   * @return MultiStringJoiner
   */
  public MultiStringJoiner merge(MultiStringJoiner str) {
    return this;
  }
}
