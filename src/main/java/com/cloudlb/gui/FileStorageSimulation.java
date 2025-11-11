package com.cloudlb.gui;

import com.cloudlb.backend.FileProcessor;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileStorageSimulation {

    private File lastUploadedDir;

    public void show(Stage parent) {
        Stage stage = new Stage();
        stage.setTitle("Distributed File Storage - CloudLB");

        Label info = new Label("📦 Upload a file → Chunk → Encrypt → Distribute across containers");
        info.setStyle("-fx-text-fill:white; -fx-font-size:14px;");

        Button upload = new Button("📂 Upload File");
        Button decrypt = new Button("🔓 Decrypt & Download");
        Button back = new Button("← Back");

        upload.setStyle("-fx-background-color:#06d6a0; -fx-text-fill:white;");
        decrypt.setStyle("-fx-background-color:#3a86ff; -fx-text-fill:white;");
        back.setStyle("-fx-background-color:#555; -fx-text-fill:white;");

        TextArea result = new TextArea();
        result.setEditable(false);
        result.setWrapText(true);
        result.setStyle("-fx-control-inner-background:#1e1e1e; -fx-text-fill:#00ff99; -fx-font-family:monospace; -fx-font-size:12px;");
        result.setPrefHeight(250);

        upload.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(stage);
            if (file == null) return;

            try {
                long start = System.currentTimeMillis();

                File storageRoot = new File(System.getProperty("user.home") + "/Desktop/cloud-lb/storage");
                if (!storageRoot.exists()) storageRoot.mkdirs();

                lastUploadedDir = new File(storageRoot, file.getName().replaceAll("\\W+", "_"));
                if (lastUploadedDir.exists()) deleteDir(lastUploadedDir);
                lastUploadedDir.mkdirs();

                result.setText("🚀 Starting file processing for: " + file.getName() + "\n");

                // 🔹 Step 1 — Chunk and encrypt
                List<File> chunks = FileProcessor.chunkAndEncrypt(file, lastUploadedDir);

                result.appendText("✅ Chunking and encryption complete:\n");
                for (File chunk : chunks) {
                    result.appendText("   • " + chunk.getName() + " (" + chunk.length()/1024 + " KB)\n");
                }

                // 🔹 Step 2 — Find containers
                List<String> containers = getRunningContainers();
                result.appendText("\n🧩 Found " + containers.size() + " running containers.\n");

                // 🔹 Step 3 — Distribute chunks
                for (int i = 0; i < chunks.size(); i++) {
                    if (i < containers.size()) {
                        String container = containers.get(i);
                        File chunk = chunks.get(i);
                        runCommand("docker cp " + chunk.getAbsolutePath() + " " + container + ":/data/");
                        result.appendText("📤 Sent " + chunk.getName() + " → " + container + "\n");
                    } else {
                        result.appendText("⚠️ Not enough containers, storing locally.\n");
                    }
                }

                long end = System.currentTimeMillis();
                result.appendText("\n⏱️ Total processing time: " + (end - start) + " ms\n");

            } catch (Exception ex) {
                ex.printStackTrace();
                result.appendText("❌ Error: " + ex.getMessage() + "\n");
            }
        });

        decrypt.setOnAction(e -> {
            if (lastUploadedDir == null) {
                result.appendText("⚠️ No uploaded file to decrypt.\n");
                return;
            }
            try {
                File output = new File(System.getProperty("user.home") + "/Desktop/Restored_" + lastUploadedDir.getName() + ".txt");
                FileProcessor.decryptAndMerge(lastUploadedDir, output);
                result.appendText("✅ File decrypted and restored: " + output.getAbsolutePath() + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                result.appendText("❌ Decrypt failed: " + ex.getMessage() + "\n");
            }
        });

        back.setOnAction(e -> stage.close());

        VBox root = new VBox(14, info, upload, decrypt, back, result);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color:#1e1e1e; -fx-padding:24;");
        stage.setScene(new Scene(root, 640, 480));
        stage.show();
    }

    private List<String> getRunningContainers() throws Exception {
        Process p = new ProcessBuilder("docker", "ps", "--filter", "name=filestore", "--format", "{{.Names}}").start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        List<String> containers = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) containers.add(line.trim());
        return containers;
    }

    private void runCommand(String cmd) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
        process.waitFor();
    }

    private void deleteDir(File dir) {
        try {
            Files.walk(dir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception ignored) {}
    }
}

