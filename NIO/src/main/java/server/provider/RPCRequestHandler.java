package server.provider;

import common.RpcRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class RPCRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Object service;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        channelHandlerContext.writeAndFlush(invoke(rpcRequest)).addListener(ChannelFutureListener.CLOSE);
    }
    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
        Object result = method.invoke(service, params);
        return result;
    }
}
