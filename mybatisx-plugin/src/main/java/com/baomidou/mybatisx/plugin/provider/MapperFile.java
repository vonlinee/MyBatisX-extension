package com.baomidou.mybatisx.plugin.provider;

import com.intellij.psi.PsiElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapperFile {

    String filename;

    PsiElement element;
}
