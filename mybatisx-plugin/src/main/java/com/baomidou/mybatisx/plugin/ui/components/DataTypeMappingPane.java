package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.model.DataTypeMappingSystem;
import com.baomidou.mybatisx.model.DataTypeSystem;
import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.baomidou.mybatisx.plugin.component.Button;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.component.SimpleComboBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class DataTypeMappingPane extends BorderPane {

    HBox hBox = new HBox();

    SimpleComboBox<String> type = new SimpleComboBox<>();
    SimpleComboBox<String> anotherType = new SimpleComboBox<>();

    public DataTypeMappingPane(DataTypeSystem typeSystem) {

        hBox.addChildren(type, anotherType);

        Button btn = new Button("Add");
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        hBox.add(btn);

        type.addItems(typeSystem.getTypeGroupIds());
        anotherType.addItems(typeSystem.getTypeGroupIds());



        addToTop(hBox);

        DataTypeMappingTable table = new DataTypeMappingTable();
        DataTypeMappingSystem typeMapping = typeSystem.getTypeMapping();

        addToCenter(table);
    }
}
