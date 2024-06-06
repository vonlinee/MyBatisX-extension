package org.mybatisx.extension.agent.client;

import com.alibaba.fastjson.JSON;
import org.mybatisx.extension.agent.Data;
import org.mybatisx.extension.agent.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

class RPCProxy implements InvocationHandler {

    private final String ip;
    private final int port;
    private final String value;
    private final Class<?> type;

    public RPCProxy(String ip, int port, String value, Class<?> type) {
        this.ip = ip;
        this.port = port;
        this.value = value;
        this.type = type;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        Log.info("trying to connect:[" + ip + ":" + port + "]");
        try (
                Socket socket = new Socket(ip, port);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            Data data = new Data(type, value, method.getReturnType(), method.getName(), method.getParameterTypes(), args);
            objectOutputStream.writeObject(data);
            Log.info("waiting for server response....");
            result = objectInputStream.readObject();
            Log.info("received data from [" + ip + ":" + port + "]:" + JSON.toJSONString(result));
        }
        return result;
    }

    public Class<?>[] covertToClass(Object[] objects) {
        if (objects == null || objects.length == 0) {
            return null;
        }
        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }
}
