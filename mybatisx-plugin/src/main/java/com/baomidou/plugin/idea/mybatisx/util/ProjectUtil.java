package com.baomidou.plugin.idea.mybatisx.util;

import java.io.*;
import java.nio.file.Files;
import java.util.Random;

public class ProjectUtil {

    static final OSInfo.OSType osType = OSInfo.getOSType();
    private static final String homePath = System.getProperty("user.home");

    public static String getPid(String javaBinDir, String projectName) throws RuntimeException {
        Process process = null;
        BufferedReader reader = null;
        try {
            process = Runtime.getRuntime().exec(javaBinDir + "jps");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream(), osType == OSInfo.OSType.WINDOWS ? "GBK" : "UTF-8"));
            String str;
            while ((str = reader.readLine()) != null) {
                String[] lineArr = str.split(" ");
                if (lineArr.length > 1 && projectName.equals(lineArr[1].trim())) {
                    return lineArr[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
            if (process != null) {
                process.destroy();
            }
        }
        throw new RuntimeException("项目名称: " + projectName + ", pid查找失败");
    }

    public static String copyToLocal(InputStream inputStream, String fileName) throws IOException {
        File file = new File(homePath, "hotswap-libs");
        if (!file.exists()) {
            boolean res = file.mkdir();
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
            if (osType == OSInfo.OSType.WINDOWS) {
                process = Runtime.getRuntime().exec("netstat -ano | findstr " + p);
            } else if (osType == OSInfo.OSType.LINUX) {
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
}
