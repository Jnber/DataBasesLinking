package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;

public class Sender {
    private String QUEUE_NAME = "BO1";

    public void send(JSONObject message) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // channel : canal de communication , liaison logique vers le serveur pour envoyer msg
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel() )
        {
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //envoie direct de message (sans utilisation des exchanges )
            channel.basicPublish( "",QUEUE_NAME, null, message.toString().getBytes());
            System.out.println("[sender] sent '"+ message + "'");
        }
    }
}


