package client;

import server.api.IHelloService;

public class ClientMain {
    public static void main(String[] args) {
        RPCClient rpcClient = new RPCClient();
        IHelloService helloService = rpcClient.proxy(IHelloService.class, "0.0.0.0", 8080);
        System.out.println(helloService.sayHello("I'm cc"));
    }
}
