package com.cloudlb.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard {

    public void start(Stage stage) {
        stage.setTitle("CloudLB — Dashboard");

        Label title = new Label("Choose a Module");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button algoBtn = new Button("Algorithm Simulation");
        Button lbBtn = new Button("Load Balancer Health");
        Button fsBtn = new Button("File Storage / Upload");

        algoBtn.setStyle(btnStyle());
        lbBtn.setStyle(btnStyle());
        fsBtn.setStyle(btnStyle());

        algoBtn.setOnAction(e -> new AlgorithmSimulation().show(stage));
        lbBtn.setOnAction(e -> new LoadBalancerSimulation().show(stage));
        fsBtn.setOnAction(e -> new FileStorageSimulation().show(stage));

        HBox buttons = new HBox(20, algoBtn, lbBtn, fsBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(25, title, buttons);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color:#1e1e1e;");

        stage.setScene(new Scene(root, 640, 360));
        stage.show();
    }

    private String btnStyle() {
        return "-fx-padding: 14 24; -fx-font-size: 14px; -fx-background-color: #3a86ff; -fx-text-fill: white; -fx-background-radius: 10;";
    }
}

