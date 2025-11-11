package com.cloudlb.backend;

import java.util.Random;

public class AutoLoadBalancer {
    private static final String TOPIC = "cloudlb/scale";

    public static void main(String[] args) {
        System.out.println("🤖 AutoLoadBalancer started.");
        Random rnd = new Random();
        while (true) {
            try {
                int load = 25 + rnd.nextInt(60); // 25..84
                System.out.println("📊 Current load: " + load + "%");
                if (load > 70) {
                    MqttBus.publish(TOPIC, "scale-up", 1);
                } else if (load < 35) {
                    MqttBus.publish(TOPIC, "scale-down", 1);
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}

