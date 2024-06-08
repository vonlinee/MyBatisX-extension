package com.baomidou.mybatisx.feat.jpa.common.type;


import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 字段
 */
public class FieldAppendType implements AppendType {
    @Override
    public String getName() {
        return AppendTypeEnum.FIELD.name();
    }

    /**
     * 允许所有区域
     *
     * @return
     */
    @Override
    public List<String> getAllowAfter() {
        return AppendTypeEnum.FIELD.getAllowedAfterList();
    }

}
