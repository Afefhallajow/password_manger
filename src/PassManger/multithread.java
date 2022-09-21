package PassManger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class multithread {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
       // ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
        ServerSocket server = new ServerSocket(5900);
        ExecutorService pool= Executors.newFixedThreadPool(20);
        while (true)
        {pool.execute( new server(server.accept()));}
    }
}
