package org.mybatisx.extension.agent.service;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.builder.Configuration;
import org.mybatisx.extension.agent.api.AgentCommandEnum;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.AgentRequest;
import org.mybatisx.extension.agent.api.Log;
import org.mybatisx.extension.agent.api.MapperHotSwapDTO;
import org.mybatisx.extension.agent.internal.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 更新整个MyBatis Xml Mapper文件
 *
 * @param <T>
 */
public class MyBatisMapperXmlHotSwapAgentHandler<T> implements AgentHandler<T> {

  private boolean isSwapStrictMap = false;

  /**
   * 重新加载mapper的xml文件，并重新解析
   *
   * @param configuration MyBatis 配置类
   * @param xmlResource   类路径开始：self/sn/main/mapper/UserMapper.xml
   * @param type          mapper类
   */
  public static void reloadMapperXml(Configuration configuration, String xmlResource, Class<?> type) throws AgentException {
    long startTime = System.currentTimeMillis();
    Set<String> loadedResources = getLoadedResources(configuration);
    loadedResources.remove(xmlResource);
    loadedResources.remove(type.toString());
    InputStream resource = configuration.getClass().getResourceAsStream("/" + xmlResource);
    XMLMapperBuilder xmlParser = new XMLMapperBuilder(resource,
      configuration,
      xmlResource,
      configuration.getSqlFragments(),
      type.getName());
    // 源码对应 com.baomidou.mybatisplus.core.MybatisConfiguration
    if ("MybatisConfiguration".equals(configuration.getClass().getSimpleName())) {
      // mybatis-plus加了重复校验， 移除所有select、insert、update、delete语句
      removeStatement(xmlResource, configuration, type);
    }
    xmlParser.parse();
    Log.info("file [%s] hotswap finished, takes %s ms", xmlResource, (System.currentTimeMillis() - startTime));
  }

  private static void removeStatement(String xmlResource, Configuration configuration, Class<?> type) {
    try {
      Field mappedStatementField = configuration.getClass().getDeclaredField("mappedStatements");
      mappedStatementField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<String, MappedStatement> statementMap = (Map<String, MappedStatement>) mappedStatementField.get(configuration);
      XPathParser xPathParser = new XPathParser(configuration.getClass().getResourceAsStream("/" + xmlResource), true, configuration.getVariables(), new XMLMapperEntityResolver());
      XNode xNode = xPathParser.evalNode("/mapper");
      List<XNode> xNodes = xNode.evalNodes("select|insert|update|delete");
      String namespace = type.getName();
      for (XNode node : xNodes) {
        String id = namespace + "." + node.getStringAttribute("id");
        statementMap.remove(id);
      }
    } catch (Exception e) {
      Log.error("failed to remove mybatis statement", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Set<String> getLoadedResources(Configuration configuration) throws AgentException {
    Class<? extends Configuration> configurationClass = configuration.getClass();
    while (configurationClass != Configuration.class) {
      // 子类向上转型, 属性是父类protected修饰的属性
      configurationClass = (Class<? extends Configuration>) configurationClass.getSuperclass();
    }
    Field field;
    try {
      field = configurationClass.getDeclaredField("loadedResources");
      field.setAccessible(true);
      return (Set<String>) field.get(configuration);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new AgentException("failed to get loadedResources collections", e);
    }
  }

  @Override
  public boolean validate() throws AgentException {
    return true;
  }

  @Override
  public void dispatch(AgentRequest<T> command) throws AgentException {
    if (Objects.requireNonNull(command.getCommandEnum()) == AgentCommandEnum.MYBATIS_MAPPER_FILE_HOTSWAP) {
      hotSwapMapperXmlFile((MapperHotSwapDTO) command.getData());
    } else {
      throw new AgentException("command is not supported: " + command.getCommandEnum());
    }
  }

  /**
   * 在目标JVM中运行
   */
  private void hotSwapMapperXmlFile(MapperHotSwapDTO dto) throws AgentException {
    if (!validate()) {
      throw new AgentException("failed to verify mapper environment");
    }
    try {
      Class<?> type = Class.forName(dto.getMapperClass());
      if (!isSwapStrictMap) {
        for (Configuration configuration : MyBatisContext.getConfigurations()) {
          MyBatisContext.swapStrictMap(configuration);
        }
        isSwapStrictMap = true;
      }
      File xmlFile = new File(dto.getMapperXmlPath());
      String absClassPath = Utils.getAbsoluteClassPath(type);
      if (absClassPath == null) {
        return;
      }
      File xmlResourceFile = Utils.searchFile(absClassPath, xmlFile.getName());
      Utils.copyStream(new FileInputStream(xmlFile), new FileOutputStream(xmlResourceFile));
      // 执行mapper热更新
      String xmlResource = xmlResourceFile.getAbsolutePath().substring(absClassPath.length());
      for (Configuration configuration : MyBatisContext.getConfigurations()) {
        reloadMapperXml(configuration, xmlResource, type);
      }
    } catch (Exception e) {
      throw new AgentException("failed to hotswap mapper xml file: " + e.getMessage(), e);
    }
  }

  public static class StrictMap<V> extends ConcurrentHashMap<String, V> {

    private static final long serialVersionUID = -4950446264854982944L;
    private final String name;

    public StrictMap(String name) {
      this.name = name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(String key, V value) {
      if (key.contains(".")) {
        final String shortKey = getShortName(key);
        if (super.get(shortKey) == null) {
          super.put(shortKey, value);
        } else {
          super.put(shortKey, (V) new StrictMap.Ambiguity(shortKey));
        }
      }
      return super.put(key, value);
    }

    @Override
    public boolean containsKey(Object key) {
      if (key == null) {
        return false;
      }

      return super.get(key) != null;
    }

    @Override
    public V get(Object key) {
      V value = super.get(key);
      if (value == null) {
        throw new IllegalArgumentException(name + " does not contain value for " + key);
      }
      if (value instanceof StrictMap.Ambiguity) {
        throw new IllegalArgumentException(((StrictMap.Ambiguity) value).getSubject() + " is ambiguous in " + name
                                           + " (try using the full name including the namespace, or rename one of the entries)");
      }
      return value;
    }

    private String getShortName(String key) {
      final String[] keyParts = key.split("\\.");
      return keyParts[keyParts.length - 1];
    }

    protected static class Ambiguity {
      private final String subject;

      public Ambiguity(String subject) {
        this.subject = subject;
      }

      public String getSubject() {
        return subject;
      }
    }
  }
}
