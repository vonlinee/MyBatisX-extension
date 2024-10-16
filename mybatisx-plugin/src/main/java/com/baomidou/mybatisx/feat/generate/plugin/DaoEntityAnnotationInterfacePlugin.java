package com.baomidou.mybatisx.feat.generate.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;

import java.util.List;

/**
 * Entity  注解
 *
 * @author ls9527
 */
public class DaoEntityAnnotationInterfacePlugin extends PluginAdapter {

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * @Entity " + getProperties().getProperty("domainName"));
        interfaze.addJavaDocLine(" */");
        return true;
    }


    @Override
    public boolean validate(List<String> list) {
        return true;
    }
}
