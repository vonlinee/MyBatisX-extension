package com.baomidou.mybatisx.util;

import com.baomidou.mybatisx.model.Param;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SqlUtils {

    private static final SqlFormatter FORMATTER = new BasicFormatterImpl();
    public static String COMMA = ",";
    public static String SEMICOLON = ";";
    public static Pattern PREPARING_PATTERN = Pattern.compile("Preparing:(.*?)(?=\n|\r|\r\n)");
    public static Pattern PARAMETER_PATTERN = Pattern.compile("Parameters:(.*?)(?=\n|\r|\r\n)");

    /**
     * using Hibernate formatter
     *
     * @param sql the executable sql
     * @return formatted executable sql
     */
    public static String format(String sql) {
        if (StringUtils.isBlank(sql)) {
            return StringUtils.EMPTY;
        }
        sql = FORMATTER.format(sql);
        return sql.endsWith(SEMICOLON) ? sql : sql + SEMICOLON;
    }

    public static String parseExecutableSqlFromMyBatisLog(String mybatisLog) {
        return parseExecutableSql(mybatisLog, true);
    }

    public static String parseExecutableSql(String mybatisLog, boolean formatSqlEnabled) {
        String executableSql = parseExecutableSql(mybatisLog);
        if (formatSqlEnabled) {
            return format(executableSql);
        }
        return executableSql;
    }

    /**
     * @param mybatisLogs the s mybatis logs
     *                    ==> Preparing: select * from table where id = ?
     *                    ==> Parameters: 123(String)
     * @return an executable sql
     */
    public static String parseExecutableSql(String mybatisLogs) {
        if (mybatisLogs == null || mybatisLogs.isEmpty()) {
            return "";
        }
        mybatisLogs = mybatisLogs.trim();
        String lineSeparator = System.lineSeparator();
        if (!mybatisLogs.endsWith(lineSeparator)) {
            mybatisLogs += lineSeparator;
        }
        String preparedSql = "";
        Matcher preparingSqlMatcher = PREPARING_PATTERN.matcher(mybatisLogs);
        if (preparingSqlMatcher.find()) {
            preparedSql = preparingSqlMatcher.group(1).trim();
        }
        Matcher paramsMatcher = PARAMETER_PATTERN.matcher(mybatisLogs);
        if (paramsMatcher.find()) {
            String params = paramsMatcher.group(1);
            if (StringUtils.isBlank(params)) {
                return format(preparedSql);
            }
            String[] paramItems = params.split(COMMA);
            for (int i = 0; i < paramItems.length; i++) {
                paramItems[i] = paramItems[i].trim();
            }
            for (String param : paramItems) {
                String value = Param.of(param).getValue();
                preparedSql = StringUtils.replaceOnce(preparedSql, "?", value);
            }
        }
        return preparedSql;
    }

    public static String getExecutableSql(String preparedSql, String params) {
        return getExecutableSql(preparedSql, params.split(","));
    }

    public static String getExecutableSql(String preparedSql, String[] params) {
        int length = preparedSql.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = preparedSql.charAt(i);
            if (c == '?') {
                String param = params[i];
                if (param == null || param.isEmpty()) {
                    sb.append(c);
                } else {
                    sb.append(Param.of(param).getValue());
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
