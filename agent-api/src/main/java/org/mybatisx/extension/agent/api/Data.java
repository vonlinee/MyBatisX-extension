package org.mybatisx.extension.agent.api;

import java.io.Serializable;
import java.util.Arrays;

public class Data implements Serializable {

    /**
     * 接口名称
     */
    private Class<?> type;

    /**
     * 接口实现class类名
     *
     * @see Class#getSimpleName()
     */
    private String name;

    /**
     * 返回类型
     */
    private Class<?> returnType;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数
     */
    private Object[] args;

    public Data(Class<?> type, String name, Class<?> returnType, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.type = type;
        this.name = name;
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Data{" +
               "type=" + type +
               ", name='" + name + '\'' +
               ", returnType=" + returnType +
               ", methodName='" + methodName + '\'' +
               ", parameterTypes=" + Arrays.toString(parameterTypes) +
               ", args=" + Arrays.toString(args) +
               '}';
    }
}
