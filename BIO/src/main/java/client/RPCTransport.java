package client;

import common.RpcRequest;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
@AllArgsConstructor
public class RPCTransport {
    private String host;
    private int port;
    public Object call(RpcRequest rpcRequest) {
        Object result = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try(Socket socket = new Socket(host, port);){
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rpcRequest);
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
            result = ois.readObject();
            System.out.println("I get "+ result);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (ois != null) {
                try {
                    ois.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (oos != null) {
                try {
                    oos.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
