package org.example.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/11 16:08
 */
public class ServerSocketTest {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(8080));
        Socket socket = ss.accept();
        InputStream inputStream = socket.getInputStream();
        inputStream.read();

    }


}
