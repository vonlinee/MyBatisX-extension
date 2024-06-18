package com.baomidou.mybatisx.util;

import com.baomidou.mybatisx.plugin.actions.Bean2DDLAction;
import com.baomidou.mybatisx.feat.bean.Field;
import com.baomidou.mybatisx.service.DdlBuilder;
import com.baomidou.mybatisx.plugin.setting.OtherSetting;

import java.util.List;

/**
 * @author breezes_y@163.com
 * @date 2021/1/30 16:48
 * @description
 */
public class DdlFormatUtil {

    public static String buildDdlScript(String tableName, List<Field> fieldList) {

        Boolean autoTranslation = OtherSetting.getInstance().myProperties.getAutoTranslationRadio();

        DdlBuilder builder = new DdlBuilder().create()
                .tableName(tableName)
                .LeftParenthesis()
                .wrap();


        int maxFieldStringLength = 0;
        int maxFieldSqlTypeStringLength = 0;
        for (Field field : fieldList) {
            if (maxFieldStringLength <= field.getTableColumn().length()) {
                maxFieldStringLength = field.getTableColumn().length();
            }
            if (maxFieldSqlTypeStringLength <= field.getSqlType().length()) {
                maxFieldSqlTypeStringLength = field.getSqlType().length();
            }
        }
        maxFieldStringLength++;
        maxFieldSqlTypeStringLength++;

        for (Field field : fieldList) {
            String tableColumn = field.getTableColumn();
            builder = builder.space(4)
                    .addColumn(String.format("%-" + maxFieldStringLength + "s", tableColumn))
                    .addType(String.format("%-" + maxFieldSqlTypeStringLength + "s", field.getSqlType()))
                    .isPrimaryKey(field.isPrimaryKey());
            if (null != field.getComment()) {
                builder.space().addComment(field.getComment());
            }
            builder.addComma()
                    .wrap();
        }

        builder = builder.remove(2)
                .wrap()
                .rightParenthesis();
        if (autoTranslation) {
            String tableNameCommend = Bean2DDLAction.translationMap
                    .getOrDefault(tableName.replace("_", " "), tableName);
            builder.space().addComment(tableNameCommend);
        }
        return builder.end();
    }

}
