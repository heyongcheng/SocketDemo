package org.example.aio.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 15:55
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        if (this.socketChannel == null) {
            this.socketChannel = socketChannel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        String req = new String(body, StandardCharsets.UTF_8);
        System.out.println("The time server receive order: " + req);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        doWrite(currentTime);
    }

    private void doWrite(String currentTime) {
        if (currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();

            socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if (attachment.hasRemaining()) {
                        socketChannel.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
