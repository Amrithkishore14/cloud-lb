package com.cloudlb.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class LoadBalancerSimulation {

    private final Random rnd = new Random();

    public void show(Stage parent) {
        Stage stage = new Stage();
        stage.setTitle("Load Balancer Health");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Container");
        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        yAxis.setLabel("Load (%)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Current Load");
        chart.getData().add(s);

        // init 4 nodes
        s.getData().add(new XYChart.Data<>("Node 1", 40));
        s.getData().add(new XYChart.Data<>("Node 2", 55));
        s.getData().add(new XYChart.Data<>("Node 3", 35));
        s.getData().add(new XYChart.Data<>("Node 4", 20));

        // Live updates (simulate)
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {
            for (var d : s.getData()) {
                int v = 15 + rnd.nextInt(80);
                d.setYValue(v);
            }
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();

        Button back = new Button("← Back");
        back.setOnAction(e -> { tl.stop(); stage.close(); });

        BorderPane root = new BorderPane(chart);
        root.setTop(back);
        BorderPane.setMargin(back, new Insets(8));
        root.setStyle("-fx-background-color:#1e1e1e;");
        chart.setStyle("-fx-background-color: transparent;");

        stage.setScene(new Scene(root, 700, 420));
        stage.show();
    }
}

