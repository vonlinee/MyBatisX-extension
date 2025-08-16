package org.mybatisx.extension.agent.internal;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

public final class Utils {

  private Utils() {
  }

  /**
   * 搜索目录下的文件
   *
   * @param dir      开始目录
   * @param fileName 文件名
   * @return 指定文件名的文件
   * @throws FileNotFoundException 文件不存在
   */
  public static File searchFile(String dir, String fileName) throws FileNotFoundException {
    LinkedList<File> fileQueue = new LinkedList<>();
    fileQueue.offerFirst(new File(dir));
    while (!fileQueue.isEmpty()) {
      File f = fileQueue.pollFirst();
      if (f.exists()) {
        if (f.isDirectory()) {
          File[] files = f.listFiles();
          if (files != null) {
            for (File listFile : files) {
              fileQueue.offer(listFile);
            }
          }
        } else if (f.getName().equals(fileName)) {
          return f;
        }
      }
    }
    throw new FileNotFoundException(fileName);
  }

  /**
   * @param fileName 文件名
   * @return 文件类路径
   * @throws FileNotFoundException 没找到文件
   */
  public static String searchFileClassPath(String dir, String fileName) throws FileNotFoundException {
    File file = searchFile(dir, fileName);
    return file.getAbsolutePath().substring(dir.length());
  }

  /**
   * 获取当前类的绝对类路径
   *
   * @return 类
   */
  public static String getAbsoluteClassPath(Class<?> clazz) {
    String name = "/" + clazz.getName().replace(".", "/") + ".class";
    URL resource = clazz.getResource(name);
    if (resource == null) {
      return null;
    }
    String path = resource.getPath();
    name = name.replace("\\", File.separator).replace("/", File.separator);
    path = path.replace("\\", File.separator).replace("/", File.separator);
    path = path.substring(0, path.indexOf(name) + 1);
    return (isWindows() && path.startsWith(File.separator)) ? path.substring(1) : path;
  }

  public static long copyStream(FileInputStream in, FileOutputStream out) throws IOException {
    try (FileChannel inChannel = in.getChannel(); FileChannel outChannel = out.getChannel()) {
      final long totalBytes = inChannel.size();
      for (long pos = 0, remaining = totalBytes; remaining > 0; ) { // 确保文件内容不会缺失
        final long writeBytes = inChannel.transferTo(pos, remaining, outChannel); // 实际传输的字节数
        pos += writeBytes;
        remaining -= writeBytes;
      }
      return totalBytes;
    }
  }

  public static void closeQuietly(Closeable closeable) {
    if (null != closeable) {
      try {
        closeable.close();
      } catch (Exception ignored) {
      }
    }
  }

  public static boolean isWindows() {
    return System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
  }
}
