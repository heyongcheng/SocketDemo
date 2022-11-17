package org.example.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 15:46
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {


    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        attachment.serverSocketChannel.accept(attachment, this);
        System.out.println("Thread: " + Thread.currentThread().getName());
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        attachment.countDownLatch.countDown();
    }
}
