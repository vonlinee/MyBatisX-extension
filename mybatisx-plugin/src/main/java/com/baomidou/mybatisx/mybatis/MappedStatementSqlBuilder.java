package com.baomidou.mybatisx.mybatis;

import org.apache.ibatis.mapping.MappedStatement;

public interface MappedStatementSqlBuilder {

    String build(MappedStatement mappedStatement, Object parameterObject);
}
