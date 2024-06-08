package com.baomidou.mybatisx.plugin.provider;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

@Setter
@Getter
public class MapperNavigationItem {
    private Project project;
    private String place;
    private String configName;
    private PsiElement element;
    private MouseEvent originEvent;

    public MapperNavigationItem(String place, @Nullable PsiElement element) {
        this.place = place;
        this.element = element;
        if (element != null) {
            this.project = element.getProject();
        }
    }
}
