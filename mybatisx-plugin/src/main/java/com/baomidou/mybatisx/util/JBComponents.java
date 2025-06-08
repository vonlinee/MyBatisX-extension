package com.baomidou.mybatisx.util;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.ui.LanguageTextField;

public class JBComponents {

  /**
   * 设置光标到文本的开头
   *
   * @param textField textField
   */
  public static void setCaretToBeginning(LanguageTextField textField) {
    if (textField != null) {
      Editor editor = textField.getEditor();
      if (editor != null) {
        CaretModel caretModel = editor.getCaretModel();
        caretModel.moveToOffset(0);
      }
    }
  }

  public static void setCaretToEnd(LanguageTextField textField) {
    textField.setCaretPosition(textField.getDocument().getTextLength());
  }
}
