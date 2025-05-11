//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.baomidou.mybatisx.plugin.component;

import java.util.StringTokenizer;

public final class ColumnSpec extends FormSpec {
  public static final FormSpec.DefaultAlignment LEFT;
  public static final FormSpec.DefaultAlignment CENTER;
  public static final FormSpec.DefaultAlignment MIDDLE;
  public static final FormSpec.DefaultAlignment RIGHT;
  public static final FormSpec.DefaultAlignment FILL;
  public static final FormSpec.DefaultAlignment DEFAULT;

  static {
    LEFT = FormSpec.LEFT_ALIGN;
    CENTER = FormSpec.CENTER_ALIGN;
    MIDDLE = CENTER;
    RIGHT = FormSpec.RIGHT_ALIGN;
    FILL = FormSpec.FILL_ALIGN;
    DEFAULT = FILL;
  }

  public ColumnSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
    super(defaultAlignment, size, resizeWeight);
  }

  public ColumnSpec(Size size) {
    super(DEFAULT, size, 0.0);
  }

  public ColumnSpec(String encodedDescription) {
    super(DEFAULT, encodedDescription);
  }

  public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs) {
    if (encodedColumnSpecs == null) {
      throw new NullPointerException("The column specification must not be null.");
    } else {
      StringTokenizer tokenizer = new StringTokenizer(encodedColumnSpecs, ", ");
      int columnCount = tokenizer.countTokens();
      ColumnSpec[] columnSpecs = new ColumnSpec[columnCount];

      for (int i = 0; i < columnCount; ++i) {
        columnSpecs[i] = new ColumnSpec(tokenizer.nextToken());
      }

      return columnSpecs;
    }
  }

  protected boolean isHorizontal() {
    return true;
  }
}
