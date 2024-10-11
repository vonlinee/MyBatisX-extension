package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.plugin.extensions.ClickableListener;
import com.baomidou.mybatisx.plugin.extensions.ExecutableListener;
import com.baomidou.mybatisx.plugin.extensions.ListSelectionListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type Ui component facade.
 *
 * @author yanglin
 */
public final class UiComponentFacade {

    private final Project project;

    private final FileEditorManager fileEditorManager;

    private UiComponentFacade(Project project) {
        this.project = project;
        this.fileEditorManager = FileEditorManager.getInstance(project);
    }

    /**
     * Gets instance.
     *
     * @param project the project
     * @return the instance
     */
    public static UiComponentFacade getInstance(@NotNull Project project) {
        return new UiComponentFacade(project);
    }

    /**
     * Show single folder selection dialog virtual file.
     *
     * @param title    the title
     * @param toSelect the to select
     * @param roots    the roots
     * @return the virtual file
     */
    public VirtualFile showSingleFolderSelectionDialog(@NotNull String title,
                                                       @Nullable VirtualFile toSelect,
                                                       @Nullable VirtualFile... roots) {
        final FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        descriptor.setTitle(title);
        if (null != roots) {
            descriptor.setRoots(roots);
        }
        return FileChooser.chooseFile(descriptor, project, toSelect);
    }

    /**
     * Show list popup with single clickable jb popup.
     *
     * @param popupTitle        the popup title
     * @param popupListener     the popup listener
     * @param clickableTitle    the clickable title
     * @param clickableListener the clickable listener
     * @param objs              the objs
     */
    public <T> void showListPopupWithSingleClickable(@NotNull String popupTitle,
                                                     @NotNull ListSelectionListener popupListener,
                                                     @NotNull String clickableTitle,
                                                     @Nullable final ClickableListener clickableListener,
                                                     @NotNull T[] objs) {
        PopupChooserBuilder<@NotNull T> builder = createListPopupBuilder(popupTitle, popupListener, objs);
        JBCheckBox checkBox = new JBCheckBox(clickableTitle);
        builder.setSouthComponent(checkBox);
        final JBPopup popup = builder.createPopup();
        if (null != clickableListener) {
            final Runnable runnable = clickableListener::clicked;
            checkBox.addActionListener(e -> {
                popup.dispose();
                setActionForExecutableListener(runnable, clickableListener);
            });
        }
        setPositionForShown(popup);
    }

    /**
     * Show list popup jb popup.
     *
     * @param title    the title
     * @param listener the listener
     * @param objs     the objs
     * @return the jb popup
     */
    public JBPopup showListPopup(@NotNull String title,
                                 @Nullable final ListSelectionListener listener,
                                 @NotNull Object[] objs) {
        PopupChooserBuilder<Object> builder = createListPopupBuilder(title, listener, objs);
        JBPopup popup = builder.createPopup();
        setPositionForShown(popup);
        return popup;
    }

    private void setPositionForShown(JBPopup popup) {
        Editor editor = fileEditorManager.getSelectedTextEditor();
        if (null != editor) {
            popup.showInBestPositionFor(editor);
        } else {
            popup.showCenteredInCurrentWindow(project);
        }
    }

    private void setActionForExecutableListener(Runnable runnable, ExecutableListener listener) {
        final Application application = ApplicationManager.getApplication();
        if (listener.isWriteAction()) {
            application.runWriteAction(runnable);
        } else {
            application.runReadAction(runnable);
        }
    }

    /**
     * Create list popup builder popup chooser builder.
     *
     * @param title    the title
     * @param listener the listener
     * @param objs     the objs
     * @return the popup chooser builder
     */
    @SafeVarargs
    public final <T> PopupChooserBuilder<T> createListPopupBuilder(@NotNull String title,
                                                                   @Nullable final ListSelectionListener listener,
                                                                   @NotNull T... objs) {
        final JBList<T> list = new JBList<>(objs);
        PopupChooserBuilder<T> builder = new PopupChooserBuilder<>(list);
        builder.setTitle(title);
        if (null != listener) {
            final Runnable runnable = () -> listener.selected(list.getSelectedIndex());
            builder.setItemChoosenCallback(() -> setActionForExecutableListener(runnable, listener));
        }
        return builder;
    }

}
