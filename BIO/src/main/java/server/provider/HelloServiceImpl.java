package server.provider;

import server.api.IHelloService;

public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String message) {
        System.out.println("Yeah, you get a message: "+ message);
        return "hello "+ message;
    }
}
