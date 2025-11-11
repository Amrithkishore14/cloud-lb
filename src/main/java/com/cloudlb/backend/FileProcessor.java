package com.cloudlb.backend;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.*;

public class FileProcessor {

    private static final int CHUNK_COUNT = 4;
    private static final String ALGORITHM = "AES";
    private static SecretKey secretKey;

    static {
        try {
            // Generate or reuse AES key
            Path keyFile = Paths.get("encryption.key");
            if (Files.exists(keyFile)) {
                byte[] keyBytes = Files.readAllBytes(keyFile);
                secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            } else {
                KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
                keyGen.init(128, new SecureRandom());
                secretKey = keyGen.generateKey();
                Files.write(keyFile, secretKey.getEncoded());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔹 Split and encrypt
    public static List<File> chunkAndEncrypt(File file, File outputDir) throws Exception {
        if (!outputDir.exists()) outputDir.mkdirs();
        List<File> chunks = new ArrayList<>();

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        int chunkSize = (int) Math.ceil((double) fileBytes.length / CHUNK_COUNT);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        for (int i = 0; i < CHUNK_COUNT; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, fileBytes.length);
            byte[] part = Arrays.copyOfRange(fileBytes, start, end);
            byte[] encrypted = cipher.doFinal(part);

            File chunkFile = new File(outputDir, "chunk" + (i + 1) + ".enc");
            Files.write(chunkFile.toPath(), encrypted);
            chunks.add(chunkFile);
        }

        return chunks;
    }

    // 🔹 Decrypt and merge
    public static File decryptAndMerge(File chunkDir, File outputFile) throws Exception {
        File[] chunks = chunkDir.listFiles((d, n) -> n.endsWith(".enc"));
        if (chunks == null || chunks.length == 0) throw new Exception("No chunks found!");

        Arrays.sort(chunks, Comparator.comparing(File::getName)); // sort chunk1,2,3,4
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (File chunk : chunks) {
                byte[] enc = Files.readAllBytes(chunk.toPath());
                byte[] dec = cipher.doFinal(enc);
                fos.write(dec);
            }
        }

        return outputFile;
    }
}

