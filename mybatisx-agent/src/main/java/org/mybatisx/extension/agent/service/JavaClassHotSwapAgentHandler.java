package org.mybatisx.extension.agent.service;

import org.mybatisx.extension.agent.api.AgentCommandEnum;
import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.AgentRequest;
import org.mybatisx.extension.agent.api.JavaClassHotSwapDTO;
import org.mybatisx.extension.agent.api.Log;
import org.mybatisx.extension.agent.javac.JavaStringCompiler;
import org.mybatisx.extension.agent.runtime.AgentContextHolder;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JavaClassHotSwapAgentHandler<T> implements AgentHandler<T> {

  private static final AgentContextHolder agentContext = AgentContextHolder.getInstance();
  private static final JavaStringCompiler compiler = new JavaStringCompiler();

  /**
   * 编译java文件
   *
   * @param filePath 文件绝对路径
   * @return class字节码
   * @throws IOException 编译失败
   */
  public static Map<String, byte[]> compileJava(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return compiler.compile(path.getFileName().toString(), new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
  }

  @Override
  public boolean validate() throws AgentException {
    return true;
  }

  @Override
  public void dispatch(AgentRequest<T> command) throws AgentException {
    if (Objects.requireNonNull(command.getCommandEnum()) == AgentCommandEnum.JAVA_CLASS_HOTSWAP) {
      JavaClassHotSwapDTO javaClassHotSwapDTO = (JavaClassHotSwapDTO) command.getData();
      Instrumentation inst = agentContext.getInst();
      try {
        Map<String, byte[]> classMap = compileJava(javaClassHotSwapDTO.getJavaFilePath());
        if (classMap != null && !classMap.isEmpty()) {
          List<ClassDefinition> definitions = new ArrayList<>();
          classMap.forEach((k, v) -> {
            try {
              definitions.add(new ClassDefinition(Class.forName(k), v));
            } catch (ClassNotFoundException e) {
              Log.error("failed to load class ", e);
            }
          });
          inst.redefineClasses(definitions.toArray(new ClassDefinition[0]));
        } else {
          throw new AgentException(String.format("failed to compile class %s", javaClassHotSwapDTO.getJavaFilePath()));
        }
      } catch (IOException | UnmodifiableClassException | ClassNotFoundException e) {
        throw new AgentException("failed to hotswap class: " + javaClassHotSwapDTO.getJavaFilePath(), e);
      }
    } else {
      throw new AgentException("command is not supported: " + command.getCommandEnum());
    }
  }
}
