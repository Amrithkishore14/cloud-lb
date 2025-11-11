package com.cloudlb.storage;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FilePartitioner {

    private static final int CHUNK_SIZE = 1024 * 10; // 10 KB per chunk (demo)

    // ✅ Split file into chunks
    public List<File> splitFile(File sourceFile) throws Exception {
        List<File> chunks = new ArrayList<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesRead;
            int partCounter = 1;

            File folder = new File("chunks");
            if (!folder.exists()) folder.mkdirs();

            while ((bytesRead = bis.read(buffer)) > 0) {
                File chunk = new File(folder, sourceFile.getName() + ".part" + partCounter++);
                try (FileOutputStream out = new FileOutputStream(chunk)) {
                    out.write(buffer, 0, bytesRead);
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }

    // ✅ Combine chunks back into a single file
    public void combineChunks(List<File> chunks, File outputFile) throws Exception {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            for (File chunk : chunks) {
                try (FileInputStream in = new FileInputStream(chunk)) {
                    byte[] buffer = new byte[CHUNK_SIZE];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) > 0) {
                        bos.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }

    // ✅ Generate CRC / Hash checksum for verification
    public String calculateChecksum(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        }
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}

