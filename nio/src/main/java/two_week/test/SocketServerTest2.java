package two_week.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author wangyang
 * @ClassName SocketTest
 * @Description TODO
 * @Date 2021/6/29 下午9:32
 * @Version 1.0
 */
public class SocketServerTest2 {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8802);
        while (true) {
            final Socket accept = serverSocket.accept();
            new Thread(() -> {
                service(accept);
            }).start();
        }
    }

    private static void service(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello world";
            printWriter.println("Content-Length:" + body.getBytes().length);
            //http 协议分为报文头和报文体,中间由两个换行符隔开相当于有一个空行
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
