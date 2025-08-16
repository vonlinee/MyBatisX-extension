//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisx.plugin.components;

import java.util.StringTokenizer;

public final class RowSpec extends FormSpec {
  public static final FormSpec.DefaultAlignment TOP;
  public static final FormSpec.DefaultAlignment CENTER;
  public static final FormSpec.DefaultAlignment BOTTOM;
  public static final FormSpec.DefaultAlignment FILL;
  public static final FormSpec.DefaultAlignment DEFAULT;

  static {
    TOP = FormSpec.TOP_ALIGN;
    CENTER = FormSpec.CENTER_ALIGN;
    BOTTOM = FormSpec.BOTTOM_ALIGN;
    FILL = FormSpec.FILL_ALIGN;
    DEFAULT = CENTER;
  }

  public RowSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
    super(defaultAlignment, size, resizeWeight);
  }

  public RowSpec(Size size) {
    super(DEFAULT, size, 0.0);
  }

  public RowSpec(String encodedDescription) {
    super(DEFAULT, encodedDescription);
  }

  public static RowSpec[] decodeSpecs(String encodedRowSpecs) {
    if (encodedRowSpecs == null) {
      throw new NullPointerException("The row specification must not be null.");
    } else {
      StringTokenizer tokenizer = new StringTokenizer(encodedRowSpecs, ", ");
      int rowCount = tokenizer.countTokens();
      RowSpec[] rowSpecs = new RowSpec[rowCount];

      for (int i = 0; i < rowCount; ++i) {
        rowSpecs[i] = new RowSpec(tokenizer.nextToken());
      }

      return rowSpecs;
    }
  }

  protected boolean isHorizontal() {
    return false;
  }
}
