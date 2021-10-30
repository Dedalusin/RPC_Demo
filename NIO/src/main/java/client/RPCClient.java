package client;

import java.lang.reflect.Proxy;

public class RPCClient {
    //创建并返回代理对象
    public <T> T proxy(Class<T> interfaceClass, String host, int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] { interfaceClass },
                new RemoteInvocationHandler(host, port));
    }
}
