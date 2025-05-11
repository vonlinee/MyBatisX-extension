package org.mybatisx.extension.agent.mybatis;

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
  public String getCurrentNamespace() {
    return currentNamespace;
  }

  @Override
  public void setCurrentNamespace(String currentNamespace) {
    this.currentNamespace = currentNamespace;
  }

  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  public Cache getCurrentCache() {
    return currentCache;
  }

  public void setCurrentCache(Cache currentCache) {
    this.currentCache = currentCache;
  }

  public boolean isUnresolvedCacheRef() {
    return unresolvedCacheRef;
  }

  public void setUnresolvedCacheRef(boolean unresolvedCacheRef) {
    this.unresolvedCacheRef = unresolvedCacheRef;
  }

  @Override
  public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets, boolean dirtySelect) {
    if (unresolvedCacheRef) {
      throw new IncompleteElementException("Cache-ref not yet resolved");
    }
    id = applyCurrentNamespace(id, false);
    final int i = id.indexOf(".");
    if (i > 0 && "null".equals(id.substring(0, i))) {
      id = getCurrentNamespace() + "." + id.substring(i + 1);
    }
    // TODO 使用的是项目自身的mybatis依赖（版本不固定），而agent声明的是编译期依赖，且版本固定，因此会存在API不兼容问题
    MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlSource, sqlCommandType)
      .resource(resource).fetchSize(fetchSize)
      .timeout(timeout)
      .statementType(statementType)
      .keyGenerator(keyGenerator)
      .keyProperty(keyProperty)
      .keyColumn(keyColumn)
      .databaseId(databaseId)
      .lang(lang)
      .resultOrdered(resultOrdered)
      .resultSets(resultSets)
      .resultSetType(resultSetType)
      .flushCacheRequired(flushCache)
      .useCache(useCache)
      .cache(currentCache);

    try {
      statementBuilder.dirtySelect(dirtySelect);
    } catch (Throwable e) {
      // ignore NoSuchMethodException
    }

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
