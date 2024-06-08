package com.baomidou.mybatisx.plugin.setting.configurable;

import com.baomidou.mybatisx.plugin.setting.DataTypeMappingTableModel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

public final class DataTypeConfigurable implements SearchableConfigurable {
    @Override
    public @NotNull String getId() {
        return getClass().getName();
    }

    @Override
    public String getDisplayName() {
        return "DataType Setting";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return null;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    JTable dataTypeMappingTable;

    public void init(JButton button) {
        button.addActionListener(e -> {
            // 创建一个文件选择器描述符，指定要选择的文件类型或目录
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
            // 创建一个文件选择器实例
            FileChooserDialog dialog =
                FileChooserFactory.getInstance().createFileChooser(descriptor, null, null);
            // 显示文件选择对话框
            VirtualFile[] files = dialog.choose(null, (VirtualFile) null);
            if (files.length == 1) {
                DataTypeMappingTableModel model = (DataTypeMappingTableModel) dataTypeMappingTable.getModel();
                model.load(new File(files[0].getPath()).toURI());
            }
        });
    }
}
