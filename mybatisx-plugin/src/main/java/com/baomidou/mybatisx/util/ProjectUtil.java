package com.baomidou.mybatisx.util;

import org.mybatisx.extension.agent.api.AgentException;
import org.mybatisx.extension.agent.api.Log;
import org.mybatisx.extension.agent.client.TargetProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Random;

public class ProjectUtil {

  private static final String USER_HOME = System.getProperty("user.home");

  private static final OSInfo.OSType os = OSInfo.getOSType();

  public static String getPid(String javaBinDir, String runProfileName) throws AgentException {
    Process process = null;
    BufferedReader reader = null;
    try {
      process = Runtime.getRuntime().exec(javaBinDir + "jps");
      reader = new BufferedReader(new InputStreamReader(process.getInputStream(), os == OSInfo.OSType.WINDOWS ? "GBK" : StandardCharsets.UTF_8.name()));
      String str;
      while ((str = reader.readLine()) != null) {
        String[] lineArr = str.split(" ");
        if (lineArr.length > 1 && runProfileName.equals(lineArr[1].trim())) {
          return lineArr[0];
        }
      }
    } catch (IOException e) {
      Log.error(String.format("failed to get pid %s %s", javaBinDir, runProfileName), e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          Log.error(String.format("failed to get pid %s %s", javaBinDir, runProfileName), e);
        }
      }
      if (process != null) {
        process.destroy();
      }
    }
    throw new AgentException("failed to find the pid of running with run profile[" + runProfileName + "]");
  }

  public static String copyToLocal(InputStream inputStream, String fileName) throws IOException {
    File file = new File(USER_HOME, "mybatisx-libs");
    if (!file.exists()) {
      if (file.mkdir()) {
        Log.info("create file ", file.getAbsolutePath());
      }
    }
    File targetFile = new File(file, fileName);
    IOUtils.copy(inputStream, Files.newOutputStream(targetFile.toPath()));
    return targetFile.getAbsolutePath();
  }

  public static int findAvailablePort() throws IOException {
    Random random = new Random();
    while (true) {
      int p = 20000 + random.nextInt(45535);
      Process process;
      if (os == OSInfo.OSType.WINDOWS) {
        process = Runtime.getRuntime().exec("netstat -ano | findstr " + p);
      } else if (os == OSInfo.OSType.LINUX) {
        process = Runtime.getRuntime().exec("netstat -tunlp | grep " + p);
      } else {
        return p;
      }
      try (
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
      ) {
        String line = bufferedReader.readLine();
        if (StringUtils.isEmpty(line)) {
          return p;
        }
      }
    }
  }

  /**
   * 获取jdk目标代理对象
   *
   * @param obj 代理对象
   * @return 目标代理对象
   */
  @SuppressWarnings("unchecked")
  public static <T> T getJdkProxyTargetObject(Object obj) throws AgentException {
    try {
      Class<?> aClass = obj.getClass();
      Field hField = aClass.getSuperclass().getDeclaredField("h");
      hField.setAccessible(true);
      TargetProxy h = (TargetProxy) hField.get(obj);
      Field tField = h.getClass().getDeclaredField("target");
      tField.setAccessible(true);
      return (T) tField.get(h);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new AgentException("没找到目标代理对象", e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> getAnnotationAttr(Object obj) throws AgentException {
    try {
      Field hField = obj.getClass().getSuperclass().getDeclaredField("h");
      hField.setAccessible(true);
      Object handler = hField.get(obj);
      Field memberValuesField = handler.getClass().getDeclaredField("memberValues");
      memberValuesField.setAccessible(true);
      return (Map<String, Object>) memberValuesField.get(handler);
    } catch (Exception e) {
      throw new AgentException("failed to get annotation info of proxy", e);
    }
  }
}
