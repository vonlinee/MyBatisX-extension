package org.mybatisx.extension.agent;

public final class Log {

    public static void info(String msg, Object... args) {
        System.out.print("[MyBatisX] ");
        System.out.printf((msg) + "%n", args);
    }

    public static void error(String msg, Throwable throwable) {
        System.out.print("[MyBatisX] " + msg + " \n");
        throwable.printStackTrace();
    }

    public static void error(String msg, Object... args) {
        System.out.print("[MyBatisX] ");
        if (args == null) {
            System.out.println(msg);
        } else if (args.length == 1) {
            if (args[0] instanceof Throwable) {
                error(msg, (Throwable) args[0]);
            } else {
                System.err.printf((msg) + "%n", args);
            }
        }
    }
}
