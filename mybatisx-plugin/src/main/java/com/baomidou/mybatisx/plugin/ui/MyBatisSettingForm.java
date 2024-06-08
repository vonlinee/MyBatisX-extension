package com.baomidou.mybatisx.plugin.ui;

import com.baomidou.mybatisx.feat.bean.TemplateInfo;
import com.baomidou.mybatisx.plugin.setting.DataTypeMappingTableModel;
import com.baomidou.mybatisx.plugin.setting.TemplateTableModel;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserDialog;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * The type Mybatis setting form.
 *
 * @author yanglin
 */
public class MyBatisSettingForm {

    /**
     * The Insert pattern text field.
     */
    public JTextField insertPatternTextField;

    /**
     * The Delete pattern text field.
     */
    public JTextField deletePatternTextField;

    /**
     * The Update pattern text field.
     */
    public JTextField updatePatternTextField;

    /**
     * The Select pattern text field.
     */
    public JTextField selectPatternTextField;

    /**
     * The Main panel.
     */
    public JPanel mainPanel;

    /**
     * default icon
     */
    public JRadioButton defaultRadioButton;
    /**
     * Bird Icon
     */
    public JRadioButton birdRadioButton;

    /**
     * data type mapping table
     */
    public JTable dataTypeMappingTable;

    /**
     * root panel
     */
    public JPanel root;

    /**
     * 模板表
     */
    private JTable templateTable;
    private JButton loadButton;

    private JPanel typeMappingTabRootPanel;

    /**
     * 初始化UI
     */
    public void initUI() {
        // 模板表
        this.templateTable.setModel(new TemplateTableModel());
        // 数据类型映射表
        this.dataTypeMappingTable.setModel(new DataTypeMappingTableModel());

        typeMappingTabRootPanel.setLayout(new BoxLayout(typeMappingTabRootPanel, BoxLayout.Y_AXIS));

        ComboBox<String> comboBox = new ComboBox<>();

        typeMappingTabRootPanel.add(comboBox, 0);

        loadButton.addActionListener(e -> {
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

    public void setTemplates(List<TemplateInfo> templates) {
        TemplateTableModel model = (TemplateTableModel) templateTable.getModel();
        for (TemplateInfo template : templates) {
            model.addRow(new Object[]{template.getId(), template.getName(), template.getPath()});
        }
    }

    public void setDataTypeMappingTable(Map<String, Map<String, String>> dataTypeMappingMap) {
        DataTypeMappingTableModel model = (DataTypeMappingTableModel) dataTypeMappingTable.getModel();
        for (Map.Entry<String, Map<String, String>> entry : dataTypeMappingMap.entrySet()) {
            for (Map.Entry<String, String> mappingEntry : entry.getValue().entrySet()) {
                model.addRow(new Object[]{entry.getKey(), mappingEntry.getKey(), mappingEntry.getValue()});
            }
        }
    }
}
