package com.dempe.chat.client;


import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;

import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/2/23
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */
public class ChatClient {

    private FutureConnection connection;

    private String uid;
    private String pwd;
    private String clientID;// 由客户端维护唯一性

    private MQTT mqtt;


    public ChatClient(String host, int port, String uid, String pwd, String clientID) throws Exception {
        this.uid = uid;
        this.pwd = pwd;
        this.clientID = clientID;
        init(host, port);
    }


    public void init(String host, int port) throws Exception {
        mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(uid);
        mqtt.setPassword(pwd);
        mqtt.setClientId(uid);
    }

    public FutureConnection connect() throws Exception {
        connection = mqtt.futureConnection();
        connection.connect();
        return connection;
    }

    public FutureConnection getConnection() {
        return connection;
    }


    public static void main(String[] args) throws Exception {
        String uid = "222222";
        String pwd = "pwd";
        String clientID = "222222222222";
        ChatClient client = new ChatClient("0.0.0.0", 8888, uid, pwd, clientID);
        FutureConnection connection = client.connect();
        boolean connected = connection.isConnected();
//        Future<Void> publish = connection.publish("", "".getBytes(), QoS.AT_LEAST_ONCE, false);
//        System.out.println(publish.await());
        TimeUnit.SECONDS.sleep(100);
        System.out.println(connected);
    }


}
