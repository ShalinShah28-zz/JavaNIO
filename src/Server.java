import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by shalin.s on 10/11/17.
 */
public class Server {
    public static void main (String [] args)
            throws IOException, InterruptedException {
        // Get the selector
        Selector selector = Selector.open();
        System.out.println("Selector is open for making connection: " + selector.isOpen());
        // Get the server socket channel and register using selector
        ServerSocketChannel SS = ServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8080);
        SS.bind(hostAddress);
        SS.configureBlocking(false);
        int ops = SS.validOps();
        SS.register(selector, ops, null);
        //SocketChannel client;
        int cnt=0;
        for (int i=0;i<10;i++) {
            System.out.println("Waiting for the select operation...");
            int noOfKeys = selector.select();
            System.out.println("The number of selected keys are: " + noOfKeys);
            Set selectedKeys = selector.selectedKeys();
            System.out.println("The size of the selected-keys set is: " + selectedKeys.size());
            Iterator itr = selectedKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey ky = (SelectionKey) itr.next();
                System.out.println("key: " + ky.channel());
                if (ky.isAcceptable()) {
                    System.out.println("Inside isAcceptable");
                    //The new client connection is accepted
                    SocketChannel client = SS.accept();
                    client.configureBlocking(false);

                    //The new connection is added to a selector
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("The new connection is accepted from the client: " + client);
                    //Thread.sleep(5000);
                }
                else if (ky.isReadable()) {
                    System.out.println("Inside isReadable");
                    // Data is read from the client
                    SocketChannel client = (SocketChannel) ky.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(5);
                    int bytesRead = client.read(buffer);
                    // If bytesRead = -1, then the client has closed the connection
                    System.out.println("total bytes read: " + bytesRead);
                    if(bytesRead == -1) {
                        ky.cancel();
                        client.close();
                    }
                    String output = new String(buffer.array()).trim();
                    System.out.println("Message read from client: " + output);
                }
                itr.remove();
            } // end of while loop
        } // end of for loop
    }

}