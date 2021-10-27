package server;

import server.api.IHelloService;
import server.provider.HelloServiceImpl;
import server.provider.RPCServer;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        IHelloService helloService = new HelloServiceImpl();
        RPCServer server = new RPCServer();
        server.start(helloService, 8080);
    }
}
