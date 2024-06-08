package com.baomidou.mybatisx.util;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class IdeSDK {

    /**
     * 打开一个类选择器
     *
     * @param project 项目
     * @param title   标题
     * @return 类选择器
     */
    public static TreeClassChooser treeClassChooser(Project project, String title) {
        return TreeClassChooserFactory.getInstance(project)
            .createWithInnerClassesScopeChooser(title, GlobalSearchScope.allScope(project), null, null);
    }

    /**
     * 选择类
     *
     * @param project      项目信息
     * @param classChooser 选择后的回调
     */
    public static void chooseClass(Project project, Consumer<PsiClass> classChooser) {
        TreeClassChooser chooser = treeClassChooser(project, "Select a Class");
        chooser.showDialog();
        Optional.ofNullable(chooser.getSelected()).ifPresent(classChooser);
    }
}
