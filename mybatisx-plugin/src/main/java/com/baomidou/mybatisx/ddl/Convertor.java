package com.baomidou.mybatisx.ddl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Convertor {

    public static String camel2Underline(String camel) {
        Set<String> changeHistory = new HashSet<>();
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(camel);
        while (matcher.find()) {
            String w = matcher.group().trim();
            if (!changeHistory.contains(w)) {
                camel = camel.replace(w, "_" + w);
                changeHistory.add(w);
            }
        }
        return camel.toUpperCase();
    }

    public static String getFieldGetterName(String fieldName) {
        String result = "get";
        result += fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return result;
    }

    public static String getGetterFieldName(String methodName) {
        String result = methodName.substring(3);
        result = result.substring(0, 1).toLowerCase() + result.substring(1);
        return result;
    }

    public static boolean isFieldGetter(String methodName) {
        if (methodName == null || methodName.trim().isEmpty()) {
            return false;
        }
        return methodName.indexOf("get") == 1;
    }
}
