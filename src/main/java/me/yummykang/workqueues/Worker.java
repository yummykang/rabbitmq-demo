package me.yummykang.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * write some dec. here.
 * Created by Demon on 2016/12/18 0018.
 */
public class Worker {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                    // make sure the message has been consumed.
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // ack:the default is true, so now to set it false to make sure if the worker dies, the message can be requeue
        // and another worker can consume it.
        channel.basicConsume(QUEUE_NAME, false, consumer);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }
}
