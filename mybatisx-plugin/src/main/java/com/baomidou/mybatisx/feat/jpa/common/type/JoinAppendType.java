package com.baomidou.mybatisx.feat.jpa.common.type;


import com.baomidou.mybatisx.feat.jpa.operate.model.AppendTypeEnum;

import java.util.List;

/**
 * 连接
 */
public class JoinAppendType implements AppendType {
    @Override
    public String getName() {
        return AppendTypeEnum.JOIN.name();
    }

    /**
     * 允许所有字段
     *
     * @return
     */
    @Override
    public List<String> getAllowAfter() {
        return AppendTypeEnum.JOIN.getAllowedAfterList();
    }

}
