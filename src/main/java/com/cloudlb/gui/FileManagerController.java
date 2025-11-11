package com.cloudlb.gui;

import com.cloudlb.backend.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileManagerController {

    @FXML private ListView<String> fileList;
    @FXML private Label statusLabel;
    private String username = "guest";

    public void setUsername(String username) {
        this.username = username;
        refreshFileList();
    }

    @FXML
    public void uploadFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose file to upload");
        File file = chooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                FileManager.uploadFile(username, file);
                refreshFileList();
                statusLabel.setText("✅ Uploaded " + file.getName());
            } catch (Exception e) {
                statusLabel.setText("❌ Upload failed: " + e.getMessage());
            }
        }
    }

    @FXML
    public void deleteFile() {
        String selected = fileList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FileManager.deleteFile(username, selected);
                refreshFileList();
                statusLabel.setText("🗑️ Deleted " + selected);
            } catch (Exception e) {
                statusLabel.setText("❌ Delete failed: " + e.getMessage());
            }
        }
    }

    private void refreshFileList() {
        fileList.getItems().clear();
        String[] files = FileManager.listUserFiles(username);
        if (files != null) fileList.getItems().addAll(files);
    }

    @FXML
    public void refreshFiles() {
        refreshFileList();
    }
}

