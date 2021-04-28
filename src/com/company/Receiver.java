package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONException;
import org.json.JSONObject;
public class Receiver {
    private final static  String EXCHANGENAME = "BO";
    public static void main (String[] args) throws Exception{

        JdbcPreparedTesting me = new JdbcPreparedTesting();
        FrameHo HO = new FrameHo(me);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // channel : canal de communication , liaison logique vers le serveur pour envoyer msg
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        )
        {
            channel.exchangeDeclare(EXCHANGENAME, "direct");
            String queue_name = channel.queueDeclare().getQueue();
            channel.queueBind(queue_name,EXCHANGENAME,"Data");
            channel.queueBind(queue_name,EXCHANGENAME,"IDS");

            System.out.println("[*] waiting for messages from"+ EXCHANGENAME +". To exit press Ctrl+C");
            //fct qui se declenche automatiquement à chaque fois qu'il y a un msg dans la file
            DeliverCallback deliverCallback = (consumerTag, delivery ) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("[x] received '" + message );
                try {
                    if (delivery.getEnvelope().getRoutingKey().equals("Data")) {
                        me.insertIntoDataBase(new JSONObject(message));
                    }
                    else{
                        me.deleteFromDataBase(new JSONObject(message));}

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            while(true)
                channel.basicConsume(queue_name, true, deliverCallback, ConsumerTag->{});
        }
    }
}
