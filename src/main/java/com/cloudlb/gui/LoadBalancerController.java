package com.cloudlb.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.*;

public class LoadBalancerController {

    @FXML
    private TextArea logArea;

    @FXML
    private BarChart<String, Number> chart;

    private final Map<String, Integer> nodeRequests = new LinkedHashMap<>();
    private final Random random = new Random();

    @FXML
    public void initialize() {
        log("✅ Load Balancer initialized.");
        chart.getData().clear();
        for (int i = 1; i <= 6; i++) nodeRequests.put("Node-" + i, 0);
        updateChart();
    }

    @FXML
    private void simulateRoundRobin() {
        simulateAlgorithm("Round Robin");
    }

    @FXML
    private void simulateFCFS() {
        simulateAlgorithm("FCFS");
    }

    @FXML
    private void simulateSJN() {
        simulateAlgorithm("SJN (Shortest Job Next)");
    }

    private void simulateAlgorithm(String name) {
        log("▶ Running " + name + " algorithm...");
        nodeRequests.replaceAll((k, v) -> 0);
        for (int i = 1; i <= 10; i++) {
            String node = "Node-" + ((i % 6) + 1);
            nodeRequests.put(node, nodeRequests.get(node) + 1);
            log("Request-" + i + " → " + node);
        }
        updateChart();
        log("✅ " + name + " simulation completed.\n");
    }

    private void updateChart() {
        Platform.runLater(() -> {
            chart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Requests");
            nodeRequests.forEach((node, count) ->
                    series.getData().add(new XYChart.Data<>(node, count)));
            chart.getData().add(series);
        });
    }

    private void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    // 🡸 Back button
    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) logArea.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cloudlb/gui/admin.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Admin Dashboard");
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

