package com.baomidou.plugin.idea.mybatisx.mybatis;

import com.baomidou.plugin.idea.mybatisx.util.SqlUtils;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @see org.apache.ibatis.scripting.defaults.DefaultParameterHandler
 */
public class DefaultMappedStatementSqlBuilder implements MappedStatementSqlBuilder {
    @Override
    public String build(MappedStatement mappedStatement, Object parameterObject) {
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        Configuration configuration = mappedStatement.getConfiguration();

        List<String> paramItems = new ArrayList<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = null;
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        if (metaObject == null) {
                            metaObject = configuration.newMetaObject(parameterObject);
                        }
                        value = metaObject.getValue(propertyName);
                    }
                    TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (value == null && jdbcType == null) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }

                    if (value == null) {
                        paramItems.add("null");
                    } else {
                        paramItems.add(value + "(" + value.getClass().getSimpleName() + ")");
                    }
                }
            }
        }

        String log = "==>  Preparing: " + boundSql.getSql().replace("\n", " ");
        log += "\n";
        log += "==> Parameters: " + StringUtils.join(paramItems, ",");
        return SqlUtils.parseExecutableSql(log);
    }
}
