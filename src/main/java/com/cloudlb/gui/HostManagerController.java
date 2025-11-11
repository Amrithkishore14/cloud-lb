package com.cloudlb.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.application.Platform;

public class HostManagerController {

    @FXML
    private TextArea logArea;

    @FXML
    public void initialize() {
        log("🖥️ Host Manager Console initialized.\n");
    }

    @FXML
    private void startHost() {
        log("[MQTT] host/start → Host started successfully ✅");
    }

    @FXML
    private void stopHost() {
        log("[MQTT] host/stop → Host stopped successfully 🛑");
    }

    @FXML
    private void viewStatus() {
        log("[MQTT] host/status → Node status: RUNNING (uptime 99.8%) ⚙️");
    }

    private void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }

    // ⬅ Back button
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

