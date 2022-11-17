package org.example.aio.client;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 17:02
 */
public class TimeClient {

    private static int port = 8080;

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        for (int i=0 ;i<10; i++) {
            new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-00" + i)
                .start();
        }

    }
}
