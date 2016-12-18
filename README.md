# rabbitmq-demo

## 1.Hello World
### 这个没什么好说的，就是一个Provier，一个Queue，一个Consumer。典型的生产者消费者模式。

## 2.Worke Queues
### 1）开启两个worker，一个provider
### 2）Round-robin dispatching
循环分发，多个worker循环接受message
### 3) Message acknowledgment
直译过来是消息确认，目的是防止worker在consume message的时候没有执行完该执行的工作，而导致消息丢失。
为了防止这一现象出现，rabbitmq提供消息确认的机制。Worker在消息consume执行结束时，会发送一个回执，如果没有，就把消息重新requeue到队列中去。
      
      boolean autoAck = false;
      channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
### 4）Message durability
消息耐久性，即持久化。当rabbitmq server宕机时，可以将消息保存到硬盘中，在重启后可以继续使用。

    boolean durable = true;
    channel.queueDeclare("task_queue", durable, false, false, null);
 
 这里需要注意的是，queue的名字不能跟之前声明的queue同名，因为如果同样的队列名字通过不同参数声明，rabbitmq将会报错。rabbitmq不允许用不同的参数重新定义
 一个队列。
 
 rabbitmq的持久化机制并不是完美的，更多信息参考[publisher confirms](https://www.rabbitmq.com/confirms.html)。
 
 
### 5）Fair dispatch
 公平分发，为了合理分发消息到workers，当一个worker没有执行完前一个消息时，将不会获得新的消息。
 
    int prefetchCount = 1;
    channel.basicQos(prefetchCount);
## 3.Publish/Subscribe
![示例图](https://raw.githubusercontent.com/yummykang/res/master/exchanges.png)
