package me.yummykang.rpc;


import com.rabbitmq.client.*;

public class RPCClient {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        try {
            ConnectionFactory cfconn = new ConnectionFactory();
            cfconn.setHost("192.168.241.129");
            cfconn.setUsername("admin");
            cfconn.setPassword("admin123");
            Connection conn = cfconn.newConnection();
            Channel ch = conn.createChannel();
            RpcClient service = new RpcClient(ch, "", RPC_QUEUE_NAME);

            System.out.println(service.stringCall("10"));
            conn.close();
        } catch (Exception e) {
            System.err.println("Main thread caught exception: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
