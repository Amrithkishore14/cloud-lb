package com.cloudlb.backend;

import java.io.*;
import java.nio.file.*;

public class FileManager {

    private static final String STORAGE_PATH = "storage/";

    public static void uploadFile(String username, File sourceFile) throws IOException {
        File userDir = new File(STORAGE_PATH + username);
        if (!userDir.exists()) userDir.mkdirs();

        Path destination = Paths.get(userDir.getAbsolutePath(), sourceFile.getName());
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("✅ File uploaded for " + username + ": " + sourceFile.getName());
    }

    public static File downloadFile(String username, String filename) {
        File file = new File(STORAGE_PATH + username + "/" + filename);
        if (file.exists()) {
            System.out.println("✅ File downloaded: " + filename);
            return file;
        } else {
            System.out.println("❌ File not found: " + filename);
            return null;
        }
    }

    public static void deleteFile(String username, String filename) throws IOException {
        File file = new File(STORAGE_PATH + username + "/" + filename);
        if (file.exists()) {
            Files.delete(file.toPath());
            System.out.println("🗑️ File deleted: " + filename);
        } else {
            System.out.println("❌ File not found: " + filename);
        }
    }

    public static String[] listUserFiles(String username) {
        File dir = new File(STORAGE_PATH + username);
        if (!dir.exists()) return new String[]{};
        return dir.list();
    }
}

