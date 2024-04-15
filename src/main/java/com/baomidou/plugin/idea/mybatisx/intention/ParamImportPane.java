package com.baomidou.plugin.idea.mybatisx.intention;

import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import com.google.gson.Gson;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParamImportPane extends JPanel {

    private static final Gson gson = new Gson();

    Editor jsonTab;
    Editor urlParamTab;
    JTabbedPane tabbedPane;

    public ParamImportPane(Project project) {

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        jsonTab = new Editor(project, JsonFileType.INSTANCE);
        tabbedPane.addTab("JSON", jsonTab);

        urlParamTab = new Editor(project, PlainTextFileType.INSTANCE);
        tabbedPane.addTab("URL参数", urlParamTab);

        // 设置选项卡布局在顶部
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        layout.addLayoutComponent(tabbedPane, BorderLayout.CENTER);

        add(tabbedPane);
    }

    /**
     * 获取解析得到的参数
     *
     * @return 参数列表
     */
    public List<ParamNode> getParams() {
        int index = tabbedPane.getSelectedIndex();
        List<ParamNode> paramNodes = new ArrayList<>();
        if (index == 0) {
            // json
            String text = jsonTab.getText();
            if (StringUtils.isBlank(text)) {
                return paramNodes;
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> map = gson.fromJson(text, Map.class);
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                flatten(result, entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Object> entry : result.entrySet()) {
                paramNodes.add(new ParamNode(entry.getKey(), String.valueOf(entry.getValue()), null));
            }
        } else if (index == 1) {
            // url参数
            String text = urlParamTab.getText();
            if (!StringUtils.isBlank(text)) {
                int i = text.indexOf("?");
                if (i >= 0) {
                    String[] nvPairs = text.substring(i).split("&");
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
