package org.mybatisx.extension.agent.impl;

import org.apache.ibatis.javassist.*;
import org.apache.ibatis.session.Configuration;
import org.mybatisx.extension.agent.api.Log;
import org.mybatisx.extension.agent.mybatis.DynamicMyBatisConfiguration;
import org.mybatisx.extension.agent.service.MyBatisContext;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @see org.apache.ibatis.session.SqlSessionFactory
 */
public final class MyBatisXTransformer implements ClassFileTransformer {

    static final String STATIC_CALL = MyBatisXTransformer.class.getName() + ".setConfiguration(this);";

    static final Set<String> CLASS_NAMES = new HashSet<>(Arrays.asList(
            "org.apache.ibatis.session.Configuration",
            "com.baomidou.mybatisplus.core.MybatisConfiguration"
    ));

    static final String METHOD_NAME = "replace";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
        String qualifiedClassName = className.replace("/", ".");
        if (qualifiedClassName.equals(DynamicMyBatisConfiguration.class.getName())) {
            return null;
        }

        if (CLASS_NAMES.contains(qualifiedClassName)) {
            try {
                final ClassPool classPool = ClassPool.getDefault();
                CtClass ctClass = classPool.get(qualifiedClassName);
                CtConstructor[] constructors = ctClass.getConstructors();
                if (constructors == null || constructors.length == 0) {
                    return null;
                } else {
                    for (CtConstructor constructor : constructors) {
                        constructor.insertAfter(STATIC_CALL);
                    }
                }

                CtClass ctClass1 = classPool.get("java.lang.String");
                CtClass ctClass2 = classPool.get("org.apache.ibatis.mapping.MappedStatement");

                CtMethod method = new CtMethod(CtClass.voidType, METHOD_NAME, new CtClass[]{ctClass1, ctClass2}, ctClass);
                method.setBody("{ if(mappedStatements.containsKey($1)) { System.out.println(\"remove\"); mappedStatements.remove($1);} mappedStatements.put($1, $2); }");
                ctClass.addMethod(method);

                return ctClass.toBytecode();
            } catch (NotFoundException | IOException | CannotCompileException e) {
                Log.error(String.format("cannot transfer class %s", qualifiedClassName), e);
            }
        }
        return null;
    }

    public static void setConfiguration(Object configuration) {
        if (configuration instanceof Configuration) {
            MyBatisContext.addConfiguration((Configuration) configuration);
            MyBatisContext.init(METHOD_NAME, configuration);
        }
    }
}
