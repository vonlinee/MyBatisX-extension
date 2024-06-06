package com.baomidou.plugin.idea.mybatisx.provider;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Getter
@Setter
public class MapperLineMarkerInfo extends LineMarkerInfo<PsiElement> {

    @NotNull
    PsiElement element;
    PsiElement[] targets;

    public MapperLineMarkerInfo(@NotNull PsiElement element, @Nullable Icon icon, PsiElement[] targets) {
        super(element, element.getTextRange(), icon, null, null, GutterIconRenderer.Alignment.CENTER);
        this.targets = targets;
        this.element = element;
    }

    @Override
    public GutterIconRenderer createGutterRenderer() {
        return new MapperXmlGutterIconRenderer(this);
    }

    @Override
    public String getLineMarkerTooltip() {
        return "Left click navigate to mapper method in mapper class, right click to open the menu";
    }
}
