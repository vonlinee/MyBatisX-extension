package com.baomidou.mybatisx.util;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Arrays;
import java.util.List;
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
    return TreeClassChooserFactory.getInstance(project).createWithInnerClassesScopeChooser(title, GlobalSearchScope.allScope(project), null, null);
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

  /**
   * 选择文件
   *
   * @param project      项目实例
   * @param fileConsumer 选择后的回调
   */
  public static void chooseSingleFile(Project project, Consumer<VirtualFile> fileConsumer) {
    // 创建一个文件选择器描述符，指定要选择的文件类型或目录
    FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
    // 创建一个文件选择器实例
    FileChooserDialog dialog = FileChooserFactory.getInstance().createFileChooser(descriptor, project, null);
    // 显示文件选择对话框
    VirtualFile[] files = dialog.choose(null, (VirtualFile) null);
    if (files.length == 1) {
      fileConsumer.accept(files[0]);
    }
  }

  /**
   * 选择单个模块
   *
   * @param project 项目实例
   * @return 选择的模块信息
   */
  public static Optional<Module> chooseSingleModule(Project project) {
    Module[] modules = ModuleManager.getInstance(project).getModules();
    ChooseModulesDialog dialog = new ChooseModulesDialog(project, Arrays.asList(modules), "Choose Module", "Choose single module");
    dialog.setSingleSelectionMode();
    dialog.setSize(400, 400);
    dialog.show();
    List<Module> chosenElements = dialog.getChosenElements();
    return chosenElements.stream().findFirst();
  }
}
