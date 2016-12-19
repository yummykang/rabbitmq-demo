package me.yummykang.rpc;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.StringRpcServer;

public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    private static int fib(int n) {
        if (n ==0) return 0;
        if (n == 1) return 1;
        return fib(n-1) + fib(n-2);
    }

    public static void main(String[] args) {
        try {
            ConnectionFactory connFactory = new ConnectionFactory();
            connFactory.setHost("192.168.241.129");
            connFactory.setUsername("admin");
            connFactory.setPassword("admin123");
            Connection conn = connFactory.newConnection();
            final Channel ch = conn.createChannel();

            ch.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            StringRpcServer server = new StringRpcServer(ch, RPC_QUEUE_NAME) {
                public String handleStringCall(String request) {
                    System.out.println("Got request: " + request);
                    return "" + fib(Integer.valueOf(request));
                }
            };
            server.mainloop();
        } catch (Exception ex) {
            System.err.println("Main thread caught exception: " + ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
