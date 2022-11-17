package org.example.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 17:04
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel client;

    private String host;

    private int port;

    private CountDownLatch countDownLatch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
        writeBuffer.put(req);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    client.write(attachment, attachment, this);
                } else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer,
                        new CompletionHandler<Integer, ByteBuffer>() {
                            @Override
                            public void completed(Integer result, ByteBuffer attachment) {
                                attachment.flip();
                                byte[] bytes = new byte[attachment.remaining()];
                                attachment.get(bytes);
                                String body = new String(bytes, StandardCharsets.UTF_8);
                                System.out.println("Now is : " + body);
                                countDownLatch.countDown();
                            }

                            @Override
                            public void failed(Throwable exc, ByteBuffer attachment) {
                                try {
                                    client.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    countDownLatch.countDown();
                                }
                            }
                        });

                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();
        }
    }
}
