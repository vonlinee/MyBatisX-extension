package com.baomidou.mybatisx.plugin.resultmap;

import com.baomidou.mybatisx.util.JavaUtils;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

final class ResultMapGenerationDialog extends DialogWrapper {

  private final Project project;
  private final ResultMapGenerationOptions options;
  private final JTextField resultMapId = new JTextField();
  private final JTextField className = new JTextField();
  private final JTextField packageName = new JTextField();
  private final JTextField tableName = new JTextField();
  private final JTextField targetDirectory = new JTextField();
  private final JCheckBox generateJava = new JCheckBox("Generate Java class");
  private final JCheckBox lombok = new JCheckBox("Use Lombok @Data");
  private final JCheckBox noArgs = new JCheckBox("Generate no-args constructor");
  private final JCheckBox jpaEntity = new JCheckBox("JPA Entity");
  private final JComboBox<String> generatedStrategy = new JComboBox<>(new String[]{"AUTO", "IDENTITY", "SEQUENCE", "TABLE"});
  private final MappingTableModel tableModel;
  private final JTable mappingTable;
  private final JTextArea preview = new JTextArea();

  ResultMapGenerationDialog(Project project, ResultMapGenerationOptions options) {
    super(project);
    this.project = project;
    this.options = options;
    resultMapId.setText(value(options.getResultMapId()));
    className.setText(value(options.getClassName()));
    packageName.setText(value(options.getPackageName()));
    tableName.setText(value(options.getTableName()));
    targetDirectory.setText(options.getTargetDirectory() == null
                            ? ""
                            : options.getTargetDirectory().getVirtualFile().getPresentableUrl());
    generateJava.setSelected(options.isGenerateJavaClass());
    lombok.setSelected(options.isUseLombok());
    noArgs.setSelected(options.isGenerateNoArgsConstructor());
    jpaEntity.setSelected(options.isJpaEntity());
    generatedStrategy.setSelectedItem(options.getGeneratedValueStrategy());
    tableModel = new MappingTableModel(options.getColumns());
    mappingTable = new JBTable(tableModel);
    mappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tableModel.addTableModelListener(e -> updatePreview());
    mappingTable.setPreferredScrollableViewportSize(new Dimension(780, 260));
    mappingTable.getColumnModel().getColumn(0).setPreferredWidth(110);
    mappingTable.getColumnModel().getColumn(1).setPreferredWidth(130);
    mappingTable.getColumnModel().getColumn(2).setPreferredWidth(150);
    mappingTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    mappingTable.getColumnModel().getColumn(4).setPreferredWidth(95);
    preview.setEditable(false);
    preview.setRows(10);
    preview.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createTitledBorder("Preview"),
      JBUI.Borders.empty(4)));
    updatePreview();
    setTitle("Generate Result Mapping");
    setOKButtonText("Apply");
    init();
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    JPanel root = new JPanel(new BorderLayout(0, 8));
    root.add(createForm(), BorderLayout.NORTH);
    root.add(new JScrollPane(mappingTable), BorderLayout.CENTER);
    root.add(new JScrollPane(preview), BorderLayout.SOUTH);
    root.setPreferredSize(new Dimension(860, 650));
    return root;
  }

  private JComponent createForm() {
    JPanel form = new JPanel(new GridBagLayout());
    GridBagConstraints left = new GridBagConstraints();
    left.gridx = 0;
    left.anchor = GridBagConstraints.WEST;
    left.insets = new Insets(2, 2, 2, 8);
    GridBagConstraints right = new GridBagConstraints();
    right.gridx = 1;
    right.weightx = 1;
    right.fill = GridBagConstraints.HORIZONTAL;
    right.insets = new Insets(2, 2, 2, 2);

    int row = 0;
    row = addRow(form, row, "ResultMap id", resultMapId, left, right);
    row = addRow(form, row, "Class name", className, left, right);
    row = addRow(form, row, "Package", packageName, left, right);
    row = addRow(form, row, "Table name", tableName, left, right);
    JPanel target = new JPanel(new BorderLayout(4, 0));
    target.add(targetDirectory, BorderLayout.CENTER);
    JButton browse = new JButton("Browse...");
    browse.addActionListener(e -> browseTargetDirectory());
    target.add(browse, BorderLayout.EAST);
    row = addRow(form, row, "Java source root", target, left, right);

    right.gridy = row++;
    form.add(generateJava, right);
    right.gridy = row++;
    form.add(lombok, right);
    right.gridy = row++;
    form.add(noArgs, right);
    right.gridy = row++;
    JPanel jpa = new JPanel(new BorderLayout(8, 0));
    jpa.add(jpaEntity, BorderLayout.WEST);
    jpa.add(new JLabel("GeneratedValue"), BorderLayout.CENTER);
    jpa.add(generatedStrategy, BorderLayout.EAST);
    form.add(jpa, right);

    JLabel lombokWarning = new JBLabel();
    if (JavaUtils.findClass(project, "lombok.Data").isEmpty()) {
      lombokWarning.setText("Lombok is not found in this project; the generated source may need the dependency.");
    }
    right.gridy = row;
    form.add(lombokWarning, right);
    if (!options.getWarnings().isEmpty()) {
      right.gridy = row + 1;
      JLabel warning = new JBLabel(String.join("  ", options.getWarnings()));
      form.add(warning, right);
    }

    generateJava.addActionListener(e -> {
      updateEnabledState();
      updatePreview();
    });
    lombok.addActionListener(e -> updatePreview());
    noArgs.addActionListener(e -> updatePreview());
    jpaEntity.addActionListener(e -> updatePreview());
    generatedStrategy.addActionListener(e -> updatePreview());
    resultMapId.addActionListener(e -> updatePreview());
    className.addActionListener(e -> updatePreview());
    packageName.addActionListener(e -> updatePreview());
    tableName.addActionListener(e -> updatePreview());
    addPreviewListener(resultMapId);
    addPreviewListener(className);
    addPreviewListener(packageName);
    addPreviewListener(tableName);
    updateEnabledState();
    return form;
  }

  private void addPreviewListener(JTextField field) {
    field.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        updatePreview();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updatePreview();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updatePreview();
      }
    });
  }

  private int addRow(JPanel panel, int row, String label, JComponent component,
                     GridBagConstraints left, GridBagConstraints right) {
    left.gridy = row;
    panel.add(new JLabel(label), left);
    right.gridy = row;
    panel.add(component, right);
    return row + 1;
  }

  private void updateEnabledState() {
    boolean enabled = generateJava.isSelected();
    className.setEnabled(enabled);
    packageName.setEnabled(enabled);
    tableName.setEnabled(enabled);
    targetDirectory.setEnabled(enabled);
    lombok.setEnabled(enabled);
    noArgs.setEnabled(enabled);
    jpaEntity.setEnabled(enabled);
    generatedStrategy.setEnabled(enabled && jpaEntity.isSelected());
  }

  private void browseTargetDirectory() {
    VirtualFile initial = options.getTargetDirectory() == null ? null : options.getTargetDirectory().getVirtualFile();
    VirtualFile selected = FileChooser.chooseFile(
      FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, initial);
    if (selected != null) {
      targetDirectory.setText(selected.getPresentableUrl());
    }
  }

  private void updatePreview() {
    if (mappingTable == null) {
      return;
    }
    refreshOptions(false);
    String xml = ResultMapTextGenerator.generate(options);
    String java = options.isGenerateJavaClass() ? JavaClassTextGenerator.generate(options) : "";
    preview.setText(xml + (java.isBlank() ? "" : "\n\n" + java));
    preview.setCaretPosition(0);
  }

  @Override
  protected void doOKAction() {
    if (!refreshOptions(true)) {
      return;
    }
    super.doOKAction();
  }

  ResultMapGenerationOptions getOptions() {
    return options;
  }

  private boolean refreshOptions(boolean validate) {
    if (mappingTable.isEditing()) {
      mappingTable.getCellEditor().stopCellEditing();
    }
    String id = resultMapId.getText().trim();
    if (validate && id.isBlank()) {
      com.intellij.openapi.ui.Messages.showErrorDialog(project, "ResultMap id cannot be empty.", "Generate Result Mapping");
      return false;
    }
    if (generateJava.isSelected() && validate && className.getText().trim().isBlank()) {
      com.intellij.openapi.ui.Messages.showErrorDialog(project, "Class name cannot be empty.", "Generate Result Mapping");
      return false;
    }
    options.setResultMapId(id);
    options.setClassName(className.getText().trim());
    options.setPackageName(packageName.getText().trim());
    options.setTableName(tableName.getText().trim());
    options.setGenerateJavaClass(generateJava.isSelected());
    options.setUseLombok(lombok.isSelected());
    options.setGenerateNoArgsConstructor(noArgs.isSelected());
    options.setJpaEntity(jpaEntity.isSelected());
    options.setGeneratedValueStrategy(String.valueOf(generatedStrategy.getSelectedItem()));
    if (generateJava.isSelected()) {
      VirtualFile selected = com.intellij.openapi.vfs.LocalFileSystem.getInstance()
        .findFileByPath(targetDirectory.getText().trim());
      if (selected != null) {
        options.setTargetDirectory(com.intellij.psi.PsiManager.getInstance(project).findDirectory(selected));
      }
      if (validate && options.getTargetDirectory() == null) {
        com.intellij.openapi.ui.Messages.showErrorDialog(project, "Please select a Java source directory.", "Generate Result Mapping");
        return false;
      }
    }
    return true;
  }

  private static String value(String text) {
    return text == null ? "" : text;
  }

  private static final class MappingTableModel extends AbstractTableModel {
    private final List<SqlColumnModel> columns;
    private final String[] names = {"Column", "Property", "Java type", "JDBC type", "ID", "Ignore"};

    private MappingTableModel(List<SqlColumnModel> columns) {
      this.columns = columns;
    }

    @Override
    public int getRowCount() {
      return columns.size();
    }

    @Override
    public int getColumnCount() {
      return names.length;
    }

    @Override
    public String getColumnName(int column) {
      return names[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
      SqlColumnModel item = columns.get(row);
      return switch (column) {
        case 0 -> item.getColumnName();
        case 1 -> item.getPropertyName();
        case 2 -> item.getJavaType();
        case 3 -> item.getJdbcType();
        case 4 -> item.isId();
        case 5 -> item.isIgnored();
        default -> "";
      };
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return true;
    }

    @Override
    public Class<?> getColumnClass(int column) {
      return column >= 4 ? Boolean.class : String.class;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
      SqlColumnModel item = columns.get(row);
      switch (column) {
        case 0 -> item.setColumnName(String.valueOf(value));
        case 1 -> item.setPropertyName(String.valueOf(value));
        case 2 -> item.setJavaType(String.valueOf(value));
        case 3 -> item.setJdbcType(String.valueOf(value));
        case 4 -> item.setId(Boolean.TRUE.equals(value));
        case 5 -> item.setIgnored(Boolean.TRUE.equals(value));
        default -> {
        }
      }
      fireTableCellUpdated(row, column);
    }
  }
}
