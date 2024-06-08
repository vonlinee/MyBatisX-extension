package com.baomidou.mybatisx.feat.mybatis;

import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @see MapperBuilderAssistant
 */
public class MyMapperBuilderAssistant extends MapperBuilderAssistant {

    private String currentNamespace;
    private String resource;
    private Cache currentCache;
    private boolean unresolvedCacheRef; // issue #676

    public MyMapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration, resource);
    }

    @Override
    public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets, boolean dirtySelect) {

        if (unresolvedCacheRef) {
            throw new IncompleteElementException("Cache-ref not yet resolved");
        }

        id = applyCurrentNamespace(id, false);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resource(resource).fetchSize(fetchSize).timeout(timeout).statementType(statementType).keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(lang).resultOrdered(resultOrdered).resultSets(resultSets).resultSetType(resultSetType).flushCacheRequired(flushCache).useCache(useCache).cache(currentCache).dirtySelect(dirtySelect);

        List<ResultMap> statementResultMaps = getStatementResultMaps(resultMap, resultType, id);

        statementBuilder.resultMaps(statementResultMaps);

        ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);
        return statement;
    }

    @Override
    public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets) {

        if (unresolvedCacheRef) {
            throw new IncompleteElementException("Cache-ref not yet resolved");
        }

        id = applyCurrentNamespace(id, false);

        List<ResultMap> statementResultMaps = getStatementResultMaps(resultMap, resultType, id);

        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType).resource(resource).fetchSize(fetchSize).timeout(timeout).statementType(statementType).keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(lang).resultOrdered(resultOrdered).resultSets(resultSets).resultMaps(statementResultMaps).resultSetType(resultSetType).flushCacheRequired(flushCache).useCache(useCache).cache(currentCache);

        ParameterMap statementParameterMap = getStatementParameterMap(parameterMap, parameterType, id);
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);
        return statement;
    }

    private ParameterMap getStatementParameterMap(String parameterMapName, Class<?> parameterTypeClass, String statementId) {
        parameterMapName = applyCurrentNamespace(parameterMapName, true);
        ParameterMap parameterMap = null;
        if (parameterMapName != null) {
            try {
                parameterMap = configuration.getParameterMap(parameterMapName);
            } catch (IllegalArgumentException e) {
                throw new IncompleteElementException("Could not find parameter map " + parameterMapName, e);
            }
        } else if (parameterTypeClass != null) {
            List<ParameterMapping> parameterMappings = new ArrayList<>();
            parameterMap = new ParameterMap.Builder(configuration, statementId + "-Inline", parameterTypeClass, parameterMappings).build();
        }
        return parameterMap;
    }

    private List<ResultMap> getStatementResultMaps(String resultMap, Class<?> resultType, String statementId) {
        resultMap = applyCurrentNamespace(resultMap, true);
        List<ResultMap> resultMaps = new ArrayList<>();
        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            for (String resultMapName : resultMapNames) {
                try {
                    resultMaps.add(configuration.getResultMap(resultMapName.trim()));
                } catch (IllegalArgumentException e) {
                    // throw new IncompleteElementException("Could not find result map '" + resultMapName + "' referenced from '" + statementId + "'", e);
                }
            }
        } else if (resultType != null) {
            ResultMap inlineResultMap = new ResultMap.Builder(configuration, statementId + "-Inline", resultType, new ArrayList<>(), null).build();
            resultMaps.add(inlineResultMap);
        }
        return resultMaps;
    }
}
