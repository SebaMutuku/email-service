package org.github.sebamutuku.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public final class FileAttachment {

    private static final int ENCRYPTION_KEY_LENGTH = 128;

    public static File createPDFFileFromBase64String(String fileName, String content, String password) throws IOException, IllegalArgumentException {
        Objects.requireNonNull(fileName, "File name cannot be null");
        Objects.requireNonNull(content, "Content cannot be null");

        if (fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }

        if (content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        File file = new File(fileName);
        System.out.printf("Processing file at path [%s]%n", file.getAbsolutePath());
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create parent directories");
            }
        }

        // Write the file if it doesn't exist
        if (!file.exists()) {
            writeBase64ToFile(file, content);
        }

        // Process the PDF document
        processPDFDocument(file, password);

        return file;
    }

    private static void writeBase64ToFile(File file, String content) throws IOException {
        byte[] decodedContent;
        try {
            decodedContent = Base64.getDecoder().decode(content);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 content", e);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedContent);
            fos.flush();
        }

        // Set file permissions after creation
        if (!file.setWritable(true, false) || !file.setReadable(true, false) || !file.setExecutable(true, false)) {
            System.err.printf("Warning: Could not set all permissions for file %s%n", file.getAbsolutePath());
        }
    }

    private static void processPDFDocument(File file, String password) throws IOException {
        if (!file.isFile()) {
            throw new IOException("Path does not point to a valid file");
        }

        try (PDDocument pdfDoc = loadPDFDocument(file, password)) {
            if (password != null && !password.isEmpty()) {
                applyDocumentProtection(pdfDoc, password);
            }
            pdfDoc.save(file);
        }
    }

    private static PDDocument loadPDFDocument(File file, String password) throws IOException {
        try {
            return password != null && !password.isEmpty() ? Loader.loadPDF(file, password) : Loader.loadPDF(file);
        } catch (IOException e) {
            throw new IOException("Failed to load PDF document" + (password != null ? " (password protected)" : ""), e);
        }
    }

    private static void applyDocumentProtection(PDDocument pdfDoc, String password) throws IOException {
        AccessPermission permission = new AccessPermission();
        permission.setCanPrint(true);
        permission.setCanModify(true);

        StandardProtectionPolicy protectionPolicy = new StandardProtectionPolicy(password, password, permission);
        protectionPolicy.setEncryptionKeyLength(ENCRYPTION_KEY_LENGTH);

        try {
            pdfDoc.protect(protectionPolicy);
        } catch (Exception e) {
            throw new IOException("Failed to apply document protection", e);
        }
    }
}