package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/11 17:19
 */
public class SocketChannelTest {

    private static Selector selector;

    public static void main(String[] args) throws IOException {

        selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(() -> {
            try {
                while (true) {
                    socketChannel.write(ByteBuffer.wrap("你好啊".getBytes(StandardCharsets.UTF_8)));
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        doRead();
    }

    private static void doRead() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                if (selectionKey.isValid() && selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    while (socketChannel.read(byteBuffer) > 0) {
                        System.out.println("客户端接受到响应: " + new String(byteBuffer.array(), StandardCharsets.UTF_8));
                    }

                }
            }
        }

    }
}
