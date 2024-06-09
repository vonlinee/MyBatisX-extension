package com.baomidou.mybatisx.plugin.ui;

import javax.swing.*;

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
}
