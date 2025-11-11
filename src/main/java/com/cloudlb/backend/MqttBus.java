package com.cloudlb.backend;

import org.eclipse.paho.client.mqttv3.*;

public class MqttBus {
    // NOTE: use localhost for reliability
    private static final String BROKER = "tcp://localhost:1883";
    private static volatile MqttClient client;

    public static synchronized MqttClient get() {
        if (client != null && client.isConnected()) return client;
        try {
            client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions opts = new MqttConnectOptions();
            opts.setAutomaticReconnect(true);
            opts.setCleanSession(true);
            opts.setConnectionTimeout(5);
            client.connect(opts);
            System.out.println("🔗 MQTT connected: " + BROKER);
        } catch (Exception e) {
            System.err.println("⚠️ MQTT not available: " + e.getMessage());
        }
        return client;
    }

    public static boolean publish(String topic, String message, int qos) {
        try {
            MqttClient c = get();
            if (c == null || !c.isConnected()) return false;
            MqttMessage m = new MqttMessage(message.getBytes());
            m.setQos(qos);
            c.publish(topic, m);
            return true;
        } catch (Exception e) {
            System.err.println("MQTT publish error: " + e.getMessage());
            return false;
        }
    }
}

