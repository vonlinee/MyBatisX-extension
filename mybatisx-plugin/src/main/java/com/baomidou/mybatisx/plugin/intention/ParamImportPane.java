package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamImportPane extends JPanel {

    private static final Gson gson = new Gson();

    Editor jsonTab;
    Editor urlParamTabEditor;
    JBTabs tabbedPane;
    MapperStatementParamTablePane table;

    public ParamImportPane(Project project, MapperStatementParamTablePane table) {
        super(new BorderLayout());
        this.table = table;

        tabbedPane = new JBTabsImpl(project);
//        tabbedPane.setTabPlacement(JTabbedPane.TOP);
//        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        jsonTab = new Editor(project, JsonFileType.INSTANCE);

        TabInfo tabInfo = new TabInfo(jsonTab);
        tabInfo.setText("JSON");
        tabbedPane.addTab(tabInfo);

        urlParamTabEditor = new Editor(project, PlainTextFileType.INSTANCE);
        TabInfo urlParamTab = new TabInfo(urlParamTabEditor);
        urlParamTab.setText("URL");
        tabbedPane.addTab(urlParamTab);

        add(tabbedPane.getComponent(), BorderLayout.CENTER);

        Box box = Box.createHorizontalBox();
        box.add(new Box.Filler(new Dimension(100, 0), new Dimension(200, 0),
            new Dimension(Short.MAX_VALUE, 0)));
        // 参数导入覆盖模式
        ComboBox<ImportModel> comboBox = new ComboBox<>(50);
        DefaultComboBoxModel<ImportModel> model = new DefaultComboBoxModel<>();
        comboBox.setModel(model);
        for (ImportModel value : ImportModel.values()) {
            model.addElement(value);
        }
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
                if (value instanceof ImportModel) {
                    ImportModel person = (ImportModel) value;
                    setText(person.getLabel());
                } else {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
                return this;
            }
        };
        comboBox.setRenderer(renderer);
        JButton btnImportParams = new JButton("参数导入");
        btnImportParams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                importParams((ImportModel) comboBox.getSelectedItem());
            }
        });
        box.add(comboBox);
        box.add(btnImportParams);

        add(box, BorderLayout.SOUTH);
    }


    /**
     * 导入参数
     *
     * @param importModel 导入模式
     */
    public void importParams(ImportModel importModel) {
        if (importModel == null) {
            importModel = ImportModel.MERGE;
        }
        List<ParamNode> params = getParams();
        if (params == null || params.isEmpty()) {
            return;
        }
        Map<String, ParamNode> paramNodeMap = new HashMap<>();
        for (ParamNode param : params) {
            paramNodeMap.put(param.getKey(), param);
        }
        fillMapperStatementParams(paramNodeMap, importModel);
    }

    /**
     * 填充参数
     *
     * @param params 参数列表，不包含嵌套形式
     * @param mode   操作类型，1-全部覆盖，2-仅追加，3-追加且覆盖，4-合并不覆盖
     */
    public void fillMapperStatementParams(Map<String, ParamNode> params, ImportModel mode) {
        table.addParams(params, mode);
    }

    public void parseJsonParams(String key, JsonElement element, List<ParamNode> paramNodes) {
        if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (key == null) {
                    parseJsonParams(entry.getKey(), entry.getValue(), paramNodes);
                } else {
                    parseJsonParams(key + "." + entry.getKey(), entry.getValue(), paramNodes);
                }
            }
        } else if (element.isJsonNull()) {
            paramNodes.add(new ParamNode(key, "null", ParamDataType.UNKNOWN));
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                paramNodes.add(new ParamNode(key, element.getAsString(), ParamDataType.BOOLEAN));
            } else if (primitive.isNumber()) {
                paramNodes.add(new ParamNode(key, element.getAsString(), ParamDataType.NUMERIC));
            } else if (primitive.isString()) {
                paramNodes.add(new ParamNode(key, element.getAsString(), ParamDataType.STRING));
            }
        } else if (element.isJsonArray()) {
            paramNodes.add(new ParamNode(key, element.getAsString(), ParamDataType.ARRAY));
        }
    }

    /**
     * 获取解析得到的参数
     *
     * @return 参数列表
     */
    public List<ParamNode> getParams() {
        TabInfo selectedTabInfo = tabbedPane.getSelectedInfo();
        if (selectedTabInfo == null) {
            return Collections.emptyList();
        }
        String selectedTabText = selectedTabInfo.getText();

        List<ParamNode> paramNodes = new ArrayList<>();
        if ("JSON".equalsIgnoreCase(selectedTabText)) {
            // json
            String text = jsonTab.getText();
            if (StringUtils.isBlank(text)) {
                return paramNodes;
            }
            parseJsonParams(null, JsonParser.parseString(text), paramNodes);
        } else if ("URL".equalsIgnoreCase(selectedTabText)) {
            // url参数
            String text = urlParamTabEditor.getText();
            if (!StringUtils.isBlank(text)) {
                int i = text.indexOf("?");
                if (i >= 0) {
                    String[] nvPairs = text.substring(i + 1).split("&");
                    for (String nvPair : nvPairs) {
                        int j = nvPair.indexOf("=");
                        if (j >= 0) {
                            paramNodes.add(new ParamNode(nvPair.substring(0, j), nvPair.substring(j + 1), null));
                        }
                    }
                } else {
                    String[] nvPairs = text.split("&");
                    for (String nvPair : nvPairs) {
                        int j = nvPair.indexOf("=");
                        if (j >= 0) {
                            paramNodes.add(new ParamNode(nvPair.substring(0, j), nvPair.substring(j + 1), null));
                        }
                    }
                }
            }
        }
        return paramNodes;
    }

    private void flatten(Map<String, Object> res, String key, Object value) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> nestMap = (Map<String, Object>) value;
            for (Map.Entry<String, Object> entry : nestMap.entrySet()) {
                flatten(res, key + "." + entry.getKey(), entry.getValue());
            }
        } else {
            res.put(key, value);
        }
    }

    static class Editor extends EditorTextField {
        public Editor(Project project, FileType fileType) {
            super(project, fileType);
        }

        @Override
        protected @NotNull EditorEx createEditor() {
            EditorEx editor = super.createEditor();
            // 水平滚动条
            HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
            // 禁用单行文本
            editor.setOneLineMode(false);
            // 垂直滚动条
            editor.setVerticalScrollbarVisible(true);
            return editor;
        }
    }
}
