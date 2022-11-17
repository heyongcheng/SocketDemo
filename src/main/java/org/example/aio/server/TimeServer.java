package org.example.aio.server;

/**
 * @author heyc
 * @version 1.0
 * @date 2022/11/14 15:07
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        AsyncTimeServerHandler timeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(timeServerHandler, "AIO-AsyncTimeServerHandler-001").start();
    }

}
