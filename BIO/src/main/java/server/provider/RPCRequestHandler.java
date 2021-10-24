package server.provider;

import common.RpcRequest;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class RPCRequestHandler implements Runnable {

    private Socket socket;

    @Override
    public void run() {
        try(ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
            //根据输入读取RPCrequest，里面存有所需调用类信息
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            //调用本地服务
            Object result = invoke(rpcRequest);
            oos.writeObject(result);
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //通过反射获取本地类和方法进行处理
        Object[] params = rpcRequest.getParams();
        Class<?>[] paramTypes = new Class[params.length];
        for(int i = 0; i < params.length; i++){
            paramTypes[i] = params[i].getClass();
        }
        //获取类
        Class<?> clazz = Class.forName(rpcRequest.getClassName());
        //获取方法
        Method method = clazz.getMethod(rpcRequest.getMethod(), paramTypes);
        //带入变量
        Object result = method.invoke(params);
        return result;
    }
}
