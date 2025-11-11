package com.cloudlb.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.CRC32;

public class StorageController {

    @FXML
    private TextArea logArea;

    @FXML
    private Label statusLabel;   // 🟢/🔴 status indicator text

    @FXML
    private HBox statusBox;      // container for LED circle (in FXML)

    private File selectedFile;
    private long savedChecksum = -1;
    private Stage currentStage;
    private boolean connected = false;

    @FXML
    public void initialize() {
        log("📦 File Storage Simulation initializing...");
        simulateConnection();
    }

    // fake connect animation
    private void simulateConnection() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(800);
                connected = true;
                Platform.runLater(() -> updateStatusLED());
                log("✅ Connected to MQTT node broker (simulated).");
                return null;
            }
        };
        new Thread(task).start();
    }

    private void updateStatusLED() {
        if (connected) {
            statusBox.setStyle("-fx-background-color: #4CAF50; -fx-background-radius: 50%; -fx-min-width:14; -fx-min-height:14;");
            statusLabel.setText("Online");
        } else {
            statusBox.setStyle("-fx-background-color: #E53935; -fx-background-radius: 50%; -fx-min-width:14; -fx-min-height:14;");
            statusLabel.setText("Offline");
        }
    }

    private String timestamp() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] ";
    }

    private void log(String msg) {
        Platform.runLater(() -> logArea.appendText(timestamp() + msg + "\n"));
    }

    @FXML
    private void splitFile() {
        if (!connected) {
            log("⚠️ Cannot split file while offline.");
            return;
        }
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select File to Split");
            selectedFile = chooser.showOpenDialog(new Stage());

            if (selectedFile != null) {
                updateTitle(selectedFile.getName());
                log("[MQTT] file/split → Splitting file: " + selectedFile.getName());

                Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        for (int i = 1; i <= 4; i++) {
                            Thread.sleep(300);
                            log("🔄 Splitting progress: " + (i * 25) + "%");
                        }
                        savedChecksum = calculateCRC(selectedFile);
                        log("✅ File split completed into 4 parts (CRC32 saved: " + savedChecksum + ")");
                        return null;
                    }
                };
                new Thread(task).start();
            } else {
                log("⚠️ No file selected.");
            }
        } catch (Exception e) {
            log("❌ Error splitting file: " + e.getMessage());
        }
    }

    @FXML
    private void combineChunks() {
        if (!connected) {
            log("⚠️ Cannot combine while offline.");
            return;
        }
        if (selectedFile == null) {
            log("⚠️ No file selected.");
            return;
        }
        log("[MQTT] file/aggregate → Combining chunks for: " + selectedFile.getName());
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 1; i <= 3; i++) {
                    Thread.sleep(400);
                    log("🔄 Combining progress: " + (i * 33) + "%");
                }
                log("✅ File recombination finished → recombined_output.txt");
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    private void verifyChecksum() {
        if (!connected) {
            log("⚠️ Cannot verify while offline.");
            return;
        }
        if (selectedFile == null) {
            log("⚠️ No file selected.");
            return;
        }
        log("[MQTT] file/checksum → Verifying CRC32 integrity for: " + selectedFile.getName());
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(600);
                long currentCRC = calculateCRC(selectedFile);
                if (savedChecksum == currentCRC) {
                    log("✅ Integrity check passed ✅ (CRC32: " + currentCRC + ")");
                } else {
                    log("❌ Integrity check failed ❌ (expected: " + savedChecksum + ", found: " + currentCRC + ")");
                    connected = false;
                    Platform.runLater(() -> updateStatusLED());
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private long calculateCRC(File file) throws Exception {
        CRC32 crc = new CRC32();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
        }
        return crc.getValue();
    }

    private void updateTitle(String name) {
        Platform.runLater(() -> {
            currentStage = (Stage) logArea.getScene().getWindow();
            currentStage.setTitle("File Storage Simulation — " + name);
        });
    }

    // ⬅ back button
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

