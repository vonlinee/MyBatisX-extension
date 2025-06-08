/*
 *    Copyright 2009-2025 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.baomidou.mybatisx.plugin.intention;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterCollector implements TokenHandler {
  private static final String PARAMETER_PROPERTIES = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

  List<Parameter> parameters;

  public List<Parameter> collect(String text) {
    parameters = new ArrayList<>();
    GenericTokenParser parser = new GenericTokenParser("#{", "}", this);
    parser.parse(text);

    parser = new GenericTokenParser("${", "}", this);
    parser.parse(text);

    return parameters;
  }

  private Parameter buildParameterMapping(String content) {
    Map<String, String> propertiesMap = parseParameterMapping(content);

    Parameter.ParameterBuilder builder = Parameter.builder();

    builder.jdbcType(propertiesMap.remove("jdbcType"));
    builder.javaType(propertiesMap.remove("javaType"));
    builder.property(propertiesMap.remove("property"));
    builder.typeHandlerAlias(propertiesMap.remove("typeHandler"));

    for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
      String name = entry.getKey();
      String value = entry.getValue();
      if ("mode".equals(name)) {
        builder.mode(value);
      } else if ("numericScale".equals(name)) {
        builder.numericScale(Integer.valueOf(value));
      } else if ("resultMap".equals(name)) {
        builder.resultMapId(value);
      } else if ("jdbcTypeName".equals(name)) {
        builder.jdbcTypeName(value);
      } else if ("expression".equals(name)) {
        throw new BuilderException("Expression based parameters are not supported yet");
      } else {
        throw new BuilderException("An invalid property '" + name + "' was found in mapping #{" + content
                                   + "}.  Valid properties are " + PARAMETER_PROPERTIES);
      }
    }
    return builder.build();
  }


  private Map<String, String> parseParameterMapping(String content) {
    try {
      return new ParameterExpression(content);
    } catch (BuilderException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new BuilderException("Parsing error was found in mapping #{" + content
                                 + "}.  Check syntax #{property|(expression), var1=value1, var2=value2, ...} ", ex);
    }
  }

  @Override
  public @Nullable String handleToken(String text, int startIndex, int endIndex, String token) {
    parameters.add(buildParameterMapping(token));
    return "?";
  }
}
