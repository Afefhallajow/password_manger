package PassManger;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class multithread_for_ca {
public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        RSA rsa =new RSA();
        rsa.generate();
        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
        ServerSocket server = new ServerSocket(8800);
        ExecutorService pool= Executors.newFixedThreadPool(20);
        while (true)
        {
        pool.execute( new ca(server.accept(),rsa));
        }
        }
}
