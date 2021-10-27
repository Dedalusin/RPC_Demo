package server.provider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class RPCServer {

//    int coreSize = Runtime.getRuntime().availableProcessors();
//    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(coreSize, coreSize + 1
//            , 1, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    public void start(Object service, int port) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("暴露服务于 port:"+port);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("成功连接");
                executorService.execute(new RPCRequestHandler(socket, service));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
