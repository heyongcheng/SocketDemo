package org.example.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 15:36
 */
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch countDownLatch;

    AsynchronousServerSocketChannel serverSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;

        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        doAccept();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        serverSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
