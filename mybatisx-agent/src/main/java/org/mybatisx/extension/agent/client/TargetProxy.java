package org.mybatisx.extension.agent.client;

import org.mybatisx.extension.agent.RPCTarget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TargetProxy implements InvocationHandler {

    private final Object target;

    public TargetProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            RPCTarget target = field.getAnnotation(RPCTarget.class);
            if (target != null) {
                // 注入带有rpc注解的成员变量中
                Object rpcProxy = getRPCProxy(field.getType(), target);
                field.set(this.target, rpcProxy);
            }
        }
        return method.invoke(target, args);
    }

    static Object getRPCProxy(Class<?> object, RPCTarget target) {
        return Proxy.newProxyInstance(
                object.getClassLoader()
                , new Class[]{object}
                , new RPCProxy(target.ip(), target.port(), target.value(), object));
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Object object) {
        Object proxy = Proxy.newProxyInstance(
                object.getClass().getClassLoader()
                , object.getClass().getInterfaces()
                , new TargetProxy(object));
        return (T) proxy;
    }
}
