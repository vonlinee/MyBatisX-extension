package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.mybatis.generator.dto.TemplateSettingDTO;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.components.CodeArea;
import com.baomidou.mybatisx.plugin.components.SplitPane;
import com.baomidou.mybatisx.plugin.components.TabPane;
import com.baomidou.mybatisx.plugin.setting.GlobalTemplateSettings;
import com.baomidou.mybatisx.plugin.setting.TemplateGroup;
import com.baomidou.mybatisx.util.Icons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 模板设置面板，布局参考 IntelliJ IDEA 的 File and Code Templates 设置页。
 */
public class TemplateSettingPane extends BorderPane {

  private static final String TEMPLATE_EXTENSION = ".ftl";
  private static final String DEFAULT_TEMPLATE = "<#-- New MyBatisX template -->\n";

  private final TemplateTreeView templateTree = new TemplateTreeView();
  private final CodeArea templateEditor = new CodeArea();
  private final JTextField configNameField = new JTextField();
  private final JTextField configFileField = new JTextField();
  private final JTextField fileNameField = new JTextField();
  private final JTextField suffixField = new JTextField();
  private final JTextField packageNameField = new JTextField();
  private final JTextField encodingField = new JTextField();
  private final JTextField basePathField = new JTextField();
  private final List<JTextField> metadataFields = List.of(
    configNameField,
    configFileField,
    fileNameField,
    suffixField,
    packageNameField,
    encodingField,
    basePathField
  );

  private final List<TemplateGroup> workingGroups = new ArrayList<>();
  private List<TemplateGroup> baselineGroups = new ArrayList<>();
  private TemplateSettingDTO currentTemplate;
  private boolean loadingTemplate;
  private boolean rebuildingTree;

  private final TabPane tabPane = new TabPane();

  public TemplateSettingPane() {
    setMinimumSize(new Dimension(0, 0));
    setPreferredSize(new Dimension(0, 0));
    setCenter(tabPane);
    buildUi();
    installEditorListeners();
    reset();
  }

  private void buildUi() {
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(createSchemePanel());

    SplitPane splitPane = new SplitPane(false, 0.34f);
    splitPane.setFirstComponent(createTemplateListPanel());
    splitPane.setSecondComponent(createTemplateEditorPanel());
    splitPane.setMinimumSize(new Dimension(0, 0));
    splitPane.setPreferredSize(new Dimension(0, 0));
    borderPane.setCenter(splitPane);
    borderPane.setMinimumSize(new Dimension(0, 0));
    borderPane.setPreferredSize(new Dimension(0, 0));

    tabPane.addTab("MyBatis Generator", borderPane);
    tabPane.setMinimumSize(new Dimension(0, 0));
    tabPane.setPreferredSize(new Dimension(0, 0));
  }

  private JPanel createSchemePanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
    panel.add(new JLabel("Scheme:"));
    JComboBox<String> scheme = new JComboBox<>(new String[]{"Default", "Project"});
    scheme.setPreferredSize(new Dimension(180, scheme.getPreferredSize().height));
    panel.add(scheme);
    return panel;
  }

  private JPanel createTemplateListPanel() {
    templateTree.getTree().addTreeSelectionListener(event -> {
      if (rebuildingTree) {
        return;
      }
      syncSelectedTemplate();
      selectTemplate(templateTree.getSelectedItem());
    });

    ToolbarDecorator decorator = ToolbarDecorator.createDecorator(templateTree.getTree())
      .setPreferredSize(new Dimension(300, -1))
      .setAddAction(button -> addTemplate())
      .setRemoveAction(button -> removeTemplate())
      .addExtraAction(new CopyTemplateAction())
      .addExtraAction(new AnAction("Expand All", "Expand all template groups", Icons.TEMPLATE_EXPAND_ALL) {
        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
          templateTree.expandAll();
        }
      })
      .addExtraAction(new AnAction("Collapse All", "Collapse all template groups", Icons.TEMPLATE_COLLAPSE_ALL) {
        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
          templateTree.collapseAll();
        }
      });
    JPanel panel = decorator.createPanel();
    panel.setMinimumSize(new Dimension(0, 0));
    panel.setPreferredSize(new Dimension(300, 0));
    return panel;
  }

  private JPanel createTemplateEditorPanel() {
    JPanel panel = new JPanel(new BorderLayout(0, 6));
    panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
    panel.add(createMetadataPanel(), BorderLayout.NORTH);

    templateEditor.setMinimumSize(new Dimension(0, 0));
    templateEditor.setPreferredSize(new Dimension(0, 0));
    panel.add(templateEditor, BorderLayout.CENTER);
    panel.setMinimumSize(new Dimension(0, 0));
    panel.setPreferredSize(new Dimension(0, 0));
    return panel;
  }

  private JPanel createMetadataPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Template Metadata"));
    addMetadataField(panel, 0, "Config Name", configNameField);
    addMetadataField(panel, 1, "Config File", configFileField);
    addMetadataField(panel, 2, "File Name", fileNameField);
    addMetadataField(panel, 3, "Suffix", suffixField);
    addMetadataField(panel, 4, "Package Name", packageNameField);
    addMetadataField(panel, 5, "Encoding", encodingField);
    addMetadataField(panel, 6, "Base Path", basePathField);
    return panel;
  }

  private static void addMetadataField(JPanel panel, int row, String label, JTextField field) {
    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.gridx = 0;
    labelConstraints.gridy = row;
    labelConstraints.anchor = GridBagConstraints.WEST;
    labelConstraints.insets = new Insets(2, 4, 2, 8);
    panel.add(new JLabel(label + ':'), labelConstraints);

    GridBagConstraints fieldConstraints = new GridBagConstraints();
    fieldConstraints.gridx = 1;
    fieldConstraints.gridy = row;
    fieldConstraints.weightx = 1;
    fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    fieldConstraints.insets = new Insets(2, 0, 2, 4);
    panel.add(field, fieldConstraints);
  }

  private void installEditorListeners() {
    DocumentListener listener = new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent event) {
        syncSelectedTemplate();
      }

      @Override
      public void removeUpdate(DocumentEvent event) {
        syncSelectedTemplate();
      }

      @Override
      public void changedUpdate(DocumentEvent event) {
        syncSelectedTemplate();
      }
    };
    for (JTextField field : metadataFields) {
      field.getDocument().addDocumentListener(listener);
    }
  }

  private TemplateTreeViewNode getSelectedTemplate() {
    return templateTree.getSelectedItem();
  }

  private void selectTemplate(TemplateTreeViewNode node) {
    loadingTemplate = true;
    try {
      if (!(node instanceof TemplateSettingDTO template)) {
        currentTemplate = null;
        clearMetadata();
        templateEditor.setText("");
        setEditorEnabled(false);
        return;
      }

      currentTemplate = template;
      configNameField.setText(valueOf(template.getConfigName()));
      configFileField.setText(valueOf(template.getConfigFile()));
      fileNameField.setText(valueOf(template.getFileName()));
      suffixField.setText(valueOf(template.getSuffix()));
      packageNameField.setText(valueOf(template.getPackageName()));
      encodingField.setText(valueOf(template.getEncoding()));
      basePathField.setText(valueOf(template.getBasePath()));
      templateEditor.setText(valueOf(template.getTemplateText()));
      setEditorEnabled(true);
    } finally {
      loadingTemplate = false;
    }
  }

  private void setEditorEnabled(boolean enabled) {
    metadataFields.forEach(field -> field.setEnabled(enabled));
    templateEditor.setEnabled(enabled);
  }

  private void clearMetadata() {
    metadataFields.forEach(field -> field.setText(""));
  }

  private void syncSelectedTemplate() {
    if (loadingTemplate || currentTemplate == null) {
      return;
    }
    currentTemplate.setConfigName(configNameField.getText().trim());
    currentTemplate.setConfigFile(configFileField.getText().trim());
    currentTemplate.setFileName(fileNameField.getText().trim());
    currentTemplate.setSuffix(suffixField.getText().trim());
    currentTemplate.setPackageName(packageNameField.getText().trim());
    currentTemplate.setEncoding(encodingField.getText().trim());
    currentTemplate.setBasePath(basePathField.getText().trim());
    currentTemplate.setTemplateText(templateEditor.getText());

    DefaultMutableTreeNode selectedNode = templateTree.getLastSelectedNode();
    if (selectedNode != null && selectedNode.getUserObject() == currentTemplate) {
      templateTree.getTreeModel().nodeChanged(selectedNode);
    }
  }

  private void addTemplate() {
    TemplateGroup group = getTargetGroup();
    if (group == null) {
      group = new TemplateGroup();
      group.setName("custom");
      workingGroups.add(group);
    }

    String name = Messages.showInputDialog(
      this,
      "Template name:",
      "Add Template",
      Messages.getQuestionIcon(),
      "new-template",
      null
    );
    if (name == null) {
      return;
    }

    TemplateSettingDTO template = createTemplate(name, group);
    if (template == null) {
      return;
    }
    group.getTemplates().add(template);
    rebuildTree(template);
  }

  private void removeTemplate() {
    syncSelectedTemplate();
    DefaultMutableTreeNode selectedNode = templateTree.getLastSelectedNode();
    if (selectedNode == null || !(selectedNode.getUserObject() instanceof TemplateSettingDTO template)) {
      return;
    }
    int result = Messages.showYesNoDialog(
      this,
      "Delete template '" + template.getName() + "'?",
      "Delete Template",
      Messages.getQuestionIcon()
    );
    if (result != Messages.YES) {
      return;
    }

    TemplateGroup group = getParentGroup(selectedNode);
    if (group != null) {
      group.getTemplates().remove(template);
    }
    rebuildTree(null);
  }

  private void copyTemplate() {
    syncSelectedTemplate();
    if (!(getSelectedTemplate() instanceof TemplateSettingDTO source)) {
      return;
    }
    TemplateGroup group = getParentGroup(templateTree.getLastSelectedNode());
    if (group == null) {
      return;
    }

    String name = Messages.showInputDialog(
      this,
      "Template name:",
      "Duplicate Template",
      Messages.getQuestionIcon(),
      uniqueName(source.getConfigName() + "Copy", group),
      null
    );
    if (name == null) {
      return;
    }

    TemplateSettingDTO copy = copyTemplateSetting(source);
    applyTemplateName(copy, name);
    if (containsTemplate(group, copy)) {
      Messages.showErrorDialog(this, "A template with this name already exists.", "Duplicate Template");
      return;
    }
    group.getTemplates().add(copy);
    rebuildTree(copy);
  }

  private TemplateSettingDTO createTemplate(String name, TemplateGroup group) {
    TemplateSettingDTO template = new TemplateSettingDTO();
    applyTemplateName(template, name);
    template.setFileName(nameWithoutExtension(template.getConfigFile()));
    template.setSuffix(".java");
    template.setEncoding(StandardCharsets.UTF_8.name());
    template.setTemplateText(DEFAULT_TEMPLATE);
    if (containsTemplate(group, template)) {
      Messages.showErrorDialog(this, "A template with this name already exists.", "Add Template");
      return null;
    }
    return template;
  }

  private static void applyTemplateName(TemplateSettingDTO template, String name) {
    String trimmedName = name.trim();
    String configFile = trimmedName.endsWith(TEMPLATE_EXTENSION)
      ? trimmedName
      : trimmedName + TEMPLATE_EXTENSION;
    template.setConfigName(nameWithoutExtension(configFile));
    template.setConfigFile(configFile);
  }

  private TemplateGroup getTargetGroup() {
    DefaultMutableTreeNode selectedNode = templateTree.getLastSelectedNode();
    if (selectedNode != null) {
      if (selectedNode.getUserObject() instanceof TemplateGroup group) {
        return group;
      }
      TemplateGroup parentGroup = getParentGroup(selectedNode);
      if (parentGroup != null) {
        return parentGroup;
      }
    }
    return workingGroups.isEmpty() ? null : workingGroups.get(0);
  }

  private TemplateGroup getParentGroup(DefaultMutableTreeNode node) {
    if (node == null || !(node.getParent() instanceof DefaultMutableTreeNode parent)) {
      return null;
    }
    return parent.getUserObject() instanceof TemplateGroup group ? group : null;
  }

  private boolean containsTemplate(TemplateGroup group, TemplateSettingDTO candidate) {
    return group.getTemplates().stream().anyMatch(template ->
      Objects.equals(template.getConfigName(), candidate.getConfigName())
        || Objects.equals(template.getConfigFile(), candidate.getConfigFile())
    );
  }

  private static String uniqueName(String name, TemplateGroup group) {
    String candidate = name;
    int suffix = 2;
    while (true) {
      String currentCandidate = candidate;
      boolean exists = group.getTemplates().stream().anyMatch(template ->
        Objects.equals(template.getConfigName(), currentCandidate)
          || Objects.equals(template.getConfigFile(), currentCandidate + TEMPLATE_EXTENSION));
      if (!exists) {
        return candidate;
      }
      candidate = name + suffix++;
    }
  }

  private void rebuildTree(TemplateTreeViewNode selected) {
    syncSelectedTemplate();
    rebuildingTree = true;
    try {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode();
      for (TemplateGroup group : workingGroups) {
        DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(group);
        root.add(groupNode);
        for (TemplateSettingDTO template : group.getTemplates()) {
          groupNode.add(new DefaultMutableTreeNode(template));
        }
      }
      templateTree.setRoot(root);
      templateTree.expandAll();
    } finally {
      rebuildingTree = false;
    }

    if (selected != null) {
      DefaultMutableTreeNode selectedNode = findNode(templateTree.getRootNode(), selected);
      if (selectedNode != null) {
        templateTree.getTree().setSelectionPath(new TreePath(selectedNode.getPath()));
        selectTemplate(selected);
      }
    } else {
      templateTree.getTree().clearSelection();
      selectTemplate(null);
    }
  }

  private static DefaultMutableTreeNode findNode(DefaultMutableTreeNode parent, Object target) {
    if (parent.getUserObject() == target) {
      return parent;
    }
    for (int i = 0; i < parent.getChildCount(); i++) {
      DefaultMutableTreeNode found = findNode((DefaultMutableTreeNode) parent.getChildAt(i), target);
      if (found != null) {
        return found;
      }
    }
    return null;
  }

  private static TemplateSettingDTO copyTemplateSetting(TemplateSettingDTO source) {
    TemplateSettingDTO copy = new TemplateSettingDTO();
    copy.setConfigName(source.getConfigName());
    copy.setConfigFile(source.getConfigFile());
    copy.setFileName(source.getFileName());
    copy.setSuffix(source.getSuffix());
    copy.setPackageName(source.getPackageName());
    copy.setEncoding(source.getEncoding());
    copy.setTemplateText(source.getTemplateText());
    copy.setBasePath(source.getBasePath());
    return copy;
  }

  private static TemplateGroup copyTemplateGroup(TemplateGroup source) {
    TemplateGroup copy = new TemplateGroup();
    copy.setName(source.getName());
    List<TemplateSettingDTO> templates = new ArrayList<>();
    for (TemplateSettingDTO template : source.getTemplates()) {
      templates.add(copyTemplateSetting(template));
    }
    copy.setTemplates(templates);
    return copy;
  }

  private static List<TemplateGroup> copyTemplateGroups(List<TemplateGroup> source) {
    List<TemplateGroup> copies = new ArrayList<>();
    for (TemplateGroup group : source) {
      copies.add(copyTemplateGroup(group));
    }
    return copies;
  }

  private static String valueOf(String value) {
    return Objects.requireNonNullElse(value, "");
  }

  private static String nameWithoutExtension(String name) {
    return name.endsWith(TEMPLATE_EXTENSION)
      ? name.substring(0, name.length() - TEMPLATE_EXTENSION.length())
      : name;
  }

  public void apply() {
    syncSelectedTemplate();
    GlobalTemplateSettings.getInstance().getState().setTemplates(copyTemplateGroups(workingGroups));
    baselineGroups = copyTemplateGroups(workingGroups);
  }

  public void reset() {
    workingGroups.clear();
    workingGroups.addAll(copyTemplateGroups(GlobalTemplateSettings.getInstance().getTemplateGroups()));
    baselineGroups = copyTemplateGroups(workingGroups);
    rebuildTree(findFirstTemplate());
  }

  private TemplateSettingDTO findFirstTemplate() {
    for (TemplateGroup group : workingGroups) {
      if (!group.getTemplates().isEmpty()) {
        return group.getTemplates().get(0);
      }
    }
    return null;
  }

  public boolean hasChanged() {
    syncSelectedTemplate();
    return !Objects.equals(workingGroups, baselineGroups);
  }

  private class CopyTemplateAction extends AnAction {
    CopyTemplateAction() {
      super(() -> "Duplicate Selected Template", PlatformIcons.COPY_ICON);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
      copyTemplate();
    }
  }
}
