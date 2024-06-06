package org.mybatisx.extension.agent.server;

import org.mybatisx.extension.agent.Data;
import org.mybatisx.extension.agent.Log;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.invoke.MethodHandles.lookup;

public class AgentServer {

    /**
     * 只会存在一个非核心线程，60秒回收，
     */
    private static final ExecutorService SERVER_THREAD_POOL = new ThreadPoolExecutor(
            0,
            1,
            60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            r -> new Thread(r, "RPC服务端线程"));

    public static void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port, 5);
        SERVER_THREAD_POOL.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    try (OutputStream outputStream = socket.getOutputStream(); InputStream inputStream = socket.getInputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                        //接受客户端数据
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        Data receiveData = (Data) objectInputStream.readObject();
                        Log.info("received from client[%s]: %s", socket.getRemoteSocketAddress(), receiveData);
                        //反射调用
                        Object result = invokeMethod(receiveData);
                        //响应
                        objectOutputStream.writeObject(result);
//                    System.out.println("服务端响应成功.");
                    }
                } catch (Exception e) {
                    Log.error("failed to handle client request", e);
                }
            }
        });
    }

    /**
     * 反射调用实现方法
     *
     * @param data data from client
     * @return response
     */
    public static Object invokeMethod(Data data) {
        //返回类型加参数类型
        MethodType methodType = MethodType.methodType(data.getReturnType(), data.getParameterTypes());
        try {

            Object impl = getRPCImpl(data);
            //除了static方法 每个方法都有一个隐式参数this
            MethodHandle methodHandle = lookup().findVirtual(data.getType(), data.getMethodName(), methodType).bindTo(impl);
            return methodHandle.invokeWithArguments(data.getArgs());
        } catch (Throwable throwable) {
            Log.error("failed to invoke with data", throwable);
        }
        return null;
    }

    /**
     * 通过SPI机制找出所有实现类
     *
     * @param data data from client
     * @return 实现类
     */
    public static Object getRPCImpl(Data data) {
        ServiceLoader<?> serviceLoader = ServiceLoader.load(data.getType());
        for (Object obj : serviceLoader) {
            if (data.getName().equals(obj.getClass().getSimpleName())) {
                return obj;
            }
        }
        throw new IllegalArgumentException(data.getType().getName() + " cannot find target implementation: " + data.getName());
    }
}
