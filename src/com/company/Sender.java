package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;

public class Sender {
    private String EXCHANGENAME = "BO";


    public void send(JSONObject message, boolean type) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // channel : canal de communication , liaison logique vers le serveur pour envoyer msg
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
        )
        {
            channel.exchangeDeclare(EXCHANGENAME,"direct");
            //envoie direct de message (sans utilisation des exchanges )
            if (type){
                channel.basicPublish( EXCHANGENAME,"Data", null, message.toString().getBytes());
            }
            else
                channel.basicPublish( EXCHANGENAME,"IDS", null, message.toString().getBytes());
            System.out.println("[sender] sent '"+ message + "'");
        }
    }
}


