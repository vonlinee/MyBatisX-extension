package com.baomidou.mybatisx.plugin.ui.dialog;

import com.baomidou.mybatisx.plugin.component.TextField;
import com.baomidou.mybatisx.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class SingleValueEditorDialog extends DialogBase {

    private final TextField textField = new TextField();

    public SingleValueEditorDialog(String title) {
        setTitle(title);
    }

    @Override
    protected final @NotNull Action getOKAction() {
        return new DialogWrapperAction("Ok") {
            @Override
            protected void doAction(ActionEvent e) {
                String text = textField.getText();
                if (StringUtils.hasText(text)) {
                    onSubmit(text);
                    closeByOk();
                }
            }
        };
    }

    public void startEdit(String text) {
        textField.setText(text);
        show();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return textField;
    }

    public abstract void onSubmit(String text);
}
