package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.json.JSONException;
import org.json.JSONObject;
public class Receiver {
    private final static  String QUEUE_NAME = "BO1";
    public static void main (String[] args) throws Exception{

        JdbcPreparedTesting me = new JdbcPreparedTesting();
        FrameHo HO = new FrameHo(me);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // channel : canal de communication , liaison logique vers le serveur pour envoyer msg
        try(Connection connection = factory.newConnection();
            Channel channel1 = connection.createChannel();
        )
        {
            channel1.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("[*] waiting for messages from"+ QUEUE_NAME +". To exit press Ctrl+C");
            //fct qui se declenche automatiquement à chaque fois qu'il y a un msg dans la file
            DeliverCallback deliverCallback = (consumerTag, delivery ) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("[x] received '" + message );
                try {
                    me.insertIntoDataBase(new JSONObject(message));
                    me.getFromDataBase();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            while(true)
                channel1.basicConsume(QUEUE_NAME, true, deliverCallback, ConsumerTag->{});
        }
    }
}
