package org.example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/11 16:47
 */
public class ServerSocketChannelTest {

    private static boolean hasWrite;

    private static Selector selector;

    public static void main(String[] args) throws IOException {

        // 创建 serverSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);

        // 创建selector
        selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();
                if (selectionKey.isValid()) {
                    if (selectionKey.isAcceptable()) {
                        doAccept(selectionKey);
                    }
                    if (selectionKey.isConnectable()) {
                        doConnect(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        doRead(selectionKey);
                    }
                    /*if (selectionKey.isWritable() && !hasWrite) {
                        doWrite(selectionKey);
                        hasWrite = true;
                    }*/
                }
            }
        }
    }

    private static void doWrite(SelectionKey selectionKey) throws IOException {
        System.out.println("************ write ***************");
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.wrap("收到！！！".getBytes(StandardCharsets.UTF_8));
        socketChannel.write(byteBuffer);
        // socketChannel.close();
    }

    private static void doRead(SelectionKey selectionKey) throws IOException {
        System.out.println("************ read ***************");
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while(socketChannel.read(byteBuffer) > 0) {
            System.out.println(String.format("接受到[%s]. 消息:%s", socketChannel.getRemoteAddress(), new String(
                byteBuffer.array(), StandardCharsets.UTF_8)));
        }
        // socketChannel.register(selector, SelectionKey.OP_WRITE);
        ByteBuffer obyteBuffer = ByteBuffer.wrap("收到！！！".getBytes(StandardCharsets.UTF_8));
        socketChannel.write(obyteBuffer);
    }

    private static void doConnect(SelectionKey selectionKey) {
        System.out.println("************ connect ***************");
    }

    // accept
    private static void doAccept(SelectionKey selectionKey) throws IOException {
        System.out.println("************ accept ***************");
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
}
