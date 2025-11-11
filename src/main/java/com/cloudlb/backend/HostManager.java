package com.cloudlb.backend;

import org.eclipse.paho.client.mqttv3.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * HostManager: Handles container scaling, maintains baseline containers,
 * and ensures that all filestore containers are ready for chunk storage.
 */
public class HostManager {
    private static final int MIN_CONTAINERS = 4;   // Always keep at least 4 containers
    private static final int MAX_CONTAINERS = 6;   // Limit to 6 containers total

    public static void main(String[] args) {
        System.out.println("✅ HostManager initialized...");
        ensureBaselineContainers();
        listenForScaleRequests();
    }

    // ✅ Ensure 4 baseline containers always exist
    private static void ensureBaselineContainers() {
        try {
            Process p = Runtime.getRuntime().exec("docker ps --filter name=filestore --format {{.Names}}");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> containers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) containers.add(line.trim());
            }

            int missing = MIN_CONTAINERS - containers.size();
            if (missing > 0) {
                System.out.println("🟨 Found only " + containers.size() + " containers, creating " + missing + " more...");
                for (int i = 1; i <= missing; i++) {
                    String name = "filestore-" + System.currentTimeMillis();
                    Process create = Runtime.getRuntime().exec("docker run -d --name " + name + " linuxserver/openssh-server");
                    create.waitFor();
                    Runtime.getRuntime().exec("docker exec " + name + " mkdir -p /data");
                    System.out.println("✅ Created base container: " + name);
                    Thread.sleep(1000);
                }
                System.out.println("🟩 Baseline containers ready.");
            } else {
                System.out.println("🟩 Baseline containers already running: " + containers.size());
            }
        } catch (Exception e) {
            System.err.println("❌ Error ensuring baseline containers: " + e.getMessage());
        }
    }

    // ✅ Listen for scale requests via MQTT
    private static void listenForScaleRequests() {
        try {
            MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.connect();
            System.out.println("✅ Listening for scale requests via MQTT...");
            client.subscribe("scale/request", (topic, msg) -> {
                String payload = new String(msg.getPayload());
                if (payload.contains("scale-up")) {
                    scaleUp();
                } else if (payload.contains("scale-down")) {
                    scaleDown();
                }
            });
        } catch (Exception e) {
            System.err.println("❌ MQTT listener error: " + e.getMessage());
        }
    }

    // ✅ Scale up (add container)
    private static void scaleUp() {
        try {
            Process p = Runtime.getRuntime().exec("docker ps --filter name=filestore --format {{.Names}}");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int count = 0;
            while (reader.readLine() != null) count++;

            if (count >= MAX_CONTAINERS) {
                System.out.println("⚠️ Maximum container limit reached (" + MAX_CONTAINERS + ")");
                return;
            }

            String name = "filestore-" + System.currentTimeMillis();
            System.out.println("🚀 Scaling up: " + name);
            Process run = Runtime.getRuntime().exec("docker run -d --name " + name + " linuxserver/openssh-server");
            run.waitFor();
            Runtime.getRuntime().exec("docker exec " + name + " mkdir -p /data");
            System.out.println("📁 Created /data directory in " + name);
            System.out.println("✅ Container " + name + " is ready to receive chunks.");

        } catch (Exception e) {
            System.err.println("❌ Scale-up error: " + e.getMessage());
        }
    }

    // ✅ Scale down (remove last container)
    private static void scaleDown() {
        try {
            Process p = Runtime.getRuntime().exec("docker ps --filter name=filestore --format {{.Names}}");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<String> containers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) containers.add(line.trim());

            if (containers.size() <= MIN_CONTAINERS) {
                System.out.println("⚠️ Minimum container limit reached (" + MIN_CONTAINERS + ")");
                return;
            }

            String remove = containers.get(containers.size() - 1);
            Runtime.getRuntime().exec("docker rm -f " + remove);
            System.out.println("🧹 Scaled down: removed " + remove);

        } catch (Exception e) {
            System.err.println("❌ Scale-down error: " + e.getMessage());
        }
    }
}

