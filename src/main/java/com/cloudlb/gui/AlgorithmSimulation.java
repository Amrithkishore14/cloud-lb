package com.cloudlb.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlgorithmSimulation {

    public void show(Stage parent) {
        Stage stage = new Stage();
        stage.setTitle("Algorithm Comparison");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Algorithm");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Efficiency (%)");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> s = new XYChart.Series<>();
        s.setName("Performance");
        chart.getData().add(s);

        ChoiceBox<String> algo = new ChoiceBox<>();
        algo.getItems().addAll("Round Robin", "Least Connections", "Weighted RR");
        algo.setValue("Round Robin");
        algo.setOnAction(e -> refresh(s, algo.getValue()));
        refresh(s, algo.getValue());

        Button back = new Button("← Back");
        back.setOnAction(e -> stage.close());

        HBox top = new HBox(10, algo, back);
        top.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(12, top, chart);
        root.setPadding(new Insets(14));
        root.setStyle("-fx-background-color:#1e1e1e;");
        chart.setStyle("-fx-background-color: transparent;");

        stage.setScene(new Scene(root, 700, 420));
        stage.show();
    }

    private void refresh(XYChart.Series<String, Number> s, String selected) {
        s.getData().clear();
        switch (selected) {
            case "Least Connections" -> {
                s.getData().add(new XYChart.Data<>("Least Connections", 92));
                s.getData().add(new XYChart.Data<>("Round Robin", 82));
                s.getData().add(new XYChart.Data<>("Weighted RR", 88));
            }
            case "Weighted RR" -> {
                s.getData().add(new XYChart.Data<>("Weighted RR", 89));
                s.getData().add(new XYChart.Data<>("Least Connections", 90));
                s.getData().add(new XYChart.Data<>("Round Robin", 83));
            }
            default -> {
                s.getData().add(new XYChart.Data<>("Round Robin", 85));
                s.getData().add(new XYChart.Data<>("Least Connections", 91));
                s.getData().add(new XYChart.Data<>("Weighted RR", 87));
            }
        }
    }
}

