import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by shalin.s on 10/11/17.
 */
public class Client1 {
    public static void main (String [] args)
            throws IOException, InterruptedException {
        InetSocketAddress hA = new InetSocketAddress("localhost", 8080);
        System.out.println("waiting...");
        SocketChannel client = SocketChannel.open(hA);
        System.out.println("The Client is sending messages to server...");
        // Sending messages to the server
        String [] msg = new String [] {"shalinshah"};
        for (int j = 0; j < msg.length; j++) {
            byte [] message = new String(msg [j]).getBytes();
            System.out.println("bytes: " + message.length);
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            System.out.println(msg [j]);
            buffer.clear();
            Thread.sleep(3000);
        }
        Thread.sleep(1000000);
        client.close();
    }
}
