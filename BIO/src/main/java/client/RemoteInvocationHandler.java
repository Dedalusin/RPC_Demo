package client;

import common.RpcRequest;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@AllArgsConstructor
public class RemoteInvocationHandler implements InvocationHandler {

    private String host;

    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //把代理对象的信息封装成rpcRequest，再通过RPCTransport.call发送获取result
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setParams(args);
        RPCTransport rpcTransport = new RPCTransport(host, port);
        return rpcTransport.call(rpcRequest);
    }
}
