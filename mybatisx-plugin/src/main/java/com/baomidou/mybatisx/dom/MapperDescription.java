package com.baomidou.mybatisx.dom;

import com.baomidou.mybatisx.dom.model.Mapper;
import com.intellij.util.xml.DomFileDescription;

/**
 * <p>
 * mapper.xml 文件属性提示
 * </p>
 *
 * @author yanglin jobob
 * @since 2018 -07-30
 */
public class MapperDescription extends DomFileDescription<Mapper> {

    /**
     * Instantiates a new Mapper description.
     */
    public MapperDescription() {
        super(Mapper.class, "mapper");
    }

    @Override
    protected void initializeFileDescription() {
        registerNamespacePolicy("MybatisXml", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
    }
}
