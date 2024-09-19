package org.github.sebamutuku.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class FileAttachment {

    public static File createPDFFileFromBase64String(String fileName, String content, String password) {
        File file = new File(fileName);
        FileOutputStream fileOutputStream;
        try {
            if (!file.exists()) {
                try {
                    fileOutputStream = new FileOutputStream(file);
                    byte[] bytes = Base64.getDecoder().decode(content);
                    fileOutputStream.write(bytes);
                    fileOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (file.isFile()) {
                PDDocument pdfDoc;
                if (password != null) {
                    pdfDoc = PDDocument.load(file, password);
                } else {
                    pdfDoc = PDDocument.load(file);
                }
                AccessPermission permission = new AccessPermission();
                permission.setCanPrint(true);
                permission.setCanModify(true);
                if (password != null) {
                    StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(password, password, permission);
                    standardProtectionPolicy.setEncryptionKeyLength(128);
                    standardProtectionPolicy.setPermissions(permission);
                    pdfDoc.protect(standardProtectionPolicy);
                }
                pdfDoc.save(file);
                pdfDoc.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
