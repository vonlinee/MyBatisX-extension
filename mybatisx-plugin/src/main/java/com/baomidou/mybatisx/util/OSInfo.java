package com.baomidou.mybatisx.util;

public class OSInfo {

    public static OSType getOSType() throws SecurityException {
        String osName = System.getProperty("os.name");
        if (osName != null) {
            if (osName.contains("Windows")) {
                return OSType.WINDOWS;
            }
            if (osName.contains("Linux")) {
                return OSType.LINUX;
            }
            if (osName.contains("Solaris") || osName.contains("SunOS")) {
                return OSType.SOLARIS;
            }
            if (osName.contains("OS X")) {
                return OSType.MAC_OSX;
            }
        }
        return OSType.UNKNOWN;
    }

    public enum OSType {
        WINDOWS,
        LINUX,
        SOLARIS,
        MAC_OSX,
        UNKNOWN
    }
}
