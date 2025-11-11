package com.cloudlb.loadbalancer;

import java.util.*;

public class LoadBalancer {
    private final List<String> nodes = Arrays.asList("Node-1", "Node-2", "Node-3", "Node-4");
    private int roundRobinIndex = 0;

    private final Queue<String> fcfsQueue = new LinkedList<>();
    private final Map<String, Integer> nodeJobTimes = new HashMap<>();
    private final Map<String, Boolean> nodeHealth = new HashMap<>();

    public LoadBalancer() {
        for (String node : nodes) {
            nodeJobTimes.put(node, 0);
            nodeHealth.put(node, true); // all nodes start healthy
        }
    }

    // ✅ Round Robin with health check
    public String getNextNodeRoundRobin() {
        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(roundRobinIndex);
            roundRobinIndex = (roundRobinIndex + 1) % nodes.size();
            if (nodeHealth.get(node)) return simulateProcessing(node);
        }
        return "❌ No healthy node available!";
    }

    // ✅ FCFS
    public String getNextNodeFCFS(String request) {
        fcfsQueue.add(request);
        for (String node : nodes) {
            if (nodeHealth.get(node)) return simulateProcessing(node);
        }
        return "❌ All nodes down!";
    }

    // ✅ Shortest Job Next (SJN)
    public String getNextNodeSJN(int jobSize) {
        String bestNode = null;
        int minTime = Integer.MAX_VALUE;

        for (String node : nodes) {
            if (nodeHealth.get(node) && nodeJobTimes.get(node) < minTime) {
                minTime = nodeJobTimes.get(node);
                bestNode = node;
            }
        }

        if (bestNode == null) return "❌ No healthy node!";
        nodeJobTimes.put(bestNode, nodeJobTimes.get(bestNode) + jobSize);
        return simulateProcessing(bestNode);
    }

    // ✅ Simulate processing time & random delay
    private String simulateProcessing(String node) {
        try {
            int delay = new Random().nextInt(2000) + 500; // 0.5–2.5s delay
            Thread.sleep(delay);
            nodeJobTimes.put(node, nodeJobTimes.get(node) + delay / 1000);
            return node + " (delay " + delay + "ms)";
        } catch (InterruptedException e) {
            return node + " (interrupted)";
        }
    }

    // ✅ Randomly fail a node
    public void randomFailNode() {
        Random r = new Random();
        String failedNode = nodes.get(r.nextInt(nodes.size()));
        nodeHealth.put(failedNode, false);
        System.out.println("⚠️ Node failed: " + failedNode);
    }

    // ✅ Recover all nodes
    public void recoverAllNodes() {
        for (String node : nodes) nodeHealth.put(node, true);
        System.out.println("✅ All nodes recovered!");
    }

    // ✅ Getters
    public Map<String, Boolean> getNodeHealth() {
        return nodeHealth;
    }

    public Map<String, Integer> getNodeJobTimes() {
        return nodeJobTimes;
    }

    public List<String> getNodes() {
        return nodes;
    }
}

