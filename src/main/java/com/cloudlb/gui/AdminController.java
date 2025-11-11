package com.cloudlb.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    public void openSimulation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cloudlb/gui/simulation/loadbalancer.fxml"));
            Scene scene = new Scene(loader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Load Balancer Simulation");
            stage.show();
            System.out.println("✅ Load Balancer Simulation opened successfully!");
        } catch (Exception e) {
            System.out.println("❌ Failed to open simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
@FXML
public void openHostManager() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cloudlb/gui/hostmanager.fxml"));
        Scene scene = new Scene(loader.load(), 500, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Host Manager Console");
        stage.show();
        System.out.println("✅ Host Manager Console opened successfully!");
    } catch (Exception e) {
        System.out.println("❌ Failed to open Host Manager Console: " + e.getMessage());
        e.printStackTrace();
    }
}

    @FXML
    public void openStorageSimulation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cloudlb/gui/simulation/storage.fxml"));
            Scene scene = new Scene(loader.load(), 500, 400);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("File Storage Simulation");
            stage.show();
            System.out.println("✅ File Storage Simulation opened successfully!");
        } catch (Exception e) {
            System.out.println("❌ Failed to open storage simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

