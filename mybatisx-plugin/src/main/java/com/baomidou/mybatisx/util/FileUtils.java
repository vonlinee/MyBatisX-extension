package com.baomidou.mybatisx.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public abstract class FileUtils {

    public static void createNewFile(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
    }

    /**
     * 获取 user.home 下面的的绝对路径
     *
     * @param pathname 相对路径
     * @return 绝对路径
     */
    public static Path getUserHomePath(String... pathname) {
        String userDir = System.getProperty("user.home");
        return Paths.get(userDir, pathname);
    }

    public static void createDirectoriesQuietly(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ignored) {

            }
        }
    }

    public static boolean isEmpty(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return false;
        }
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.findAny().isPresent();
        }
    }

    /**
     * 删除目录及子目录
     *
     * @param dir 文件夹
     * @throws IOException IO异常
     */
    public static void cleanDirectory(Path dir) throws IOException {
        org.apache.commons.io.FileUtils.cleanDirectory(dir.toFile());
    }

    /**
     * 删除目录及子目录
     *
     * @param dir 文件夹
     * @throws IOException IO异常
     */
    public static void cleanDirectory(File dir) throws IOException {
        org.apache.commons.io.FileUtils.cleanDirectory(dir);
    }

    /**
     * 获取路径的文件名称
     *
     * @param path 路径
     * @return 文件名称，如果是文件则不包含后缀名
     */
    public static String getName(Path path) {
        String fileName = path.getFileName().toString();
        if (Files.isDirectory(path)) {
            return fileName;
        }
        int i = fileName.lastIndexOf(".");
        if (i >= 0) {
            return fileName.substring(0, i);
        }
        return fileName;
    }

    public static String getAbsolutePathname(Path path) {
        return path.toAbsolutePath().toString();
    }

    public static String readString(File file) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
