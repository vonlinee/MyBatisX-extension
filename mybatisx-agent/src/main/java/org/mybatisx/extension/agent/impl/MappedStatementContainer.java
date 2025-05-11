package org.mybatisx.extension.agent.impl;

import org.apache.ibatis.mapping.MappedStatement;
import org.mybatisx.extension.agent.api.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public final class MappedStatementContainer {

  /**
   * 新增的方法名
   */
  public static final String METHOD_NAME = "putMappedStatement";

  /**
   * Map<String, MappedStatement>
   */
  private final Map<String, Object> strictMap;
  private Method method;

  @SuppressWarnings("unchecked")
  public MappedStatementContainer(Object strictMap) {
    this.strictMap = (Map<String, Object>) strictMap;
    try {
      method = strictMap.getClass().getMethod(METHOD_NAME, String.class, MappedStatement.class);
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
    } catch (NoSuchMethodException e) {
      Log.error("failed to init ", e);
    }
  }

  public boolean contains(String id) {
    return strictMap.containsKey(id);
  }

  public void put(String id, MappedStatement mappedStatement) {
    try {
      Object[] param = {id, mappedStatement};
      method.invoke(strictMap, param);
    } catch (IllegalAccessException | InvocationTargetException e) {
      Log.error(String.format("failed to replace MappedStatement[%s]", id), e);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(strictMap);
  }
}
