package com.kcbgroup.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class FileAttachment {

    public static void main(String[] args) {
        String fileName = "idnumber_user_test.pdf";
        String content = "This is a textForm";
        String data = "JVBERi0xLjQNCjUgMCBvYmoNCjw8DQovVHlwZSAvUGFnZQ0KL1BhcmVudCAzIDAgUg0KL1Jlc291cmNlcyA0IDAgUg0KL0NvbnRlbnRzIDYgMCBSDQovTWVkaWFCb3hbIDAgMCA1OTUuMzUgODQxLjk1IF0NCi9Dcm9wQm94WyAwIDAgNTk1LjM1IDg0MS45NSBdDQovUm90YXRlIDANCj4+DQplbmRvYmoNCjYgMCBvYmoNCjw8IC9MZW5ndGggNDIzIC9GaWx0ZXIgL0ZsYXRlRGVjb2RlID4+DQpzdHJlYW0NCniclZTbTsJAEIbvm/Qd5hJvxp097y2KhISAwb4AalUMSFIxvo6P6m67wlag2vSih5nZ7/9nBhgwfy3G9Y1B9ZxnwyLPqH4jEBqMEGiUg2KTZ5c3lGdADIqnPBt8XRSvh1QuDTKmwJBDJ2zM5yGfN/nDyXQ6mY1hMbqdL4q6eORZaI2Gzzw7HGBRaw0bENKikIcva7gLvHB5xc1DUCyUVwiGCVRer0ML5B+hKsFj2d8OmcG9YJEYnFePZQWzj819WUWvR+zggEih2guwpgc+lNpUwWBYLd8eXmC23JRdTM54yiQUqgc2VLexjdfr5a6bagW6tNO9oFYiqRPUYtXtVWiRDJi8dtcDK8mc9Hq1fd91UaXWqPdUZ7GPV8UVSkXtya7W65oKnWbrZdaOITte5rhpMdretLgRP7HfG9GMLjm3jx0hJSpGvlwhp/TPwDU/lWK7W67/McPIP5phbHYM/6PZoROiHo4gCNky5vr+qVPflZbBv/XO6wA3rdYE+KlY0H0mFDTLMyd6fx3qSIcSxX2lc3BgJdJDMAm1JXI/Xd/MffQb0XQnBA0KZW5kc3RyZWFtDQplbmRvYmoNCjEgMCBvYmoNCjw8DQovVHlwZSAvQ2F0YWxvZw0KL1BhZ2VzIDMgMCBSDQo+Pg0KZW5kb2JqDQoyIDAgb2JqDQo8PA0KL1R5cGUgL0luZm8NCi9Qcm9kdWNlciAoT3JhY2xlIEJJIFB1Ymxpc2hlciAxMC4xLjMuNC4xKQ0KPj4NCmVuZG9iag0KMyAwIG9iag0KPDwNCi9UeXBlIC9QYWdlcw0KL0tpZHMgWw0KNSAwIFINCl0NCi9Db3VudCAxDQo+Pg0KZW5kb2JqDQo0IDAgb2JqDQo8PA0KL1Byb2NTZXQgWyAvUERGIC9UZXh0IF0NCi9Gb250IDw8IA0KL0YxIDcgMCBSDQovRjIgOCAwIFINCi9GMyA5IDAgUg0KPj4NCj4+DQplbmRvYmoNCjcgMCBvYmoNCjw8DQovVHlwZSAvRm9udA0KL1N1YnR5cGUgL1R5cGUxDQovQmFzZUZvbnQgL1RpbWVzLVJvbWFuDQovRW5jb2RpbmcgL1dpbkFuc2lFbmNvZGluZw0KPj4NCmVuZG9iag0KOCAwIG9iag0KPDwNCi9UeXBlIC9Gb250DQovU3VidHlwZSAvVHlwZTENCi9CYXNlRm9udCAvSGVsdmV0aWNhLUJvbGQNCi9FbmNvZGluZyAvV2luQW5zaUVuY29kaW5nDQo+Pg0KZW5kb2JqDQo5IDAgb2JqDQo8PA0KL1R5cGUgL0ZvbnQNCi9TdWJ0eXBlIC9UeXBlMQ0KL0Jhc2VGb250IC9UaW1lcy1Cb2xkDQovRW5jb2RpbmcgL1dpbkFuc2lFbmNvZGluZw0KPj4NCmVuZG9iag0KMTAgMCBvYmoNClsgNSAwIFIgL1hZWiAzNi4wIDY3Ny4wOTQgbnVsbCBdDQplbmRvYmoNCjExIDAgb2JqDQpbIDUgMCBSIC9YWVogMzYuMCA2NzcuMDk0IG51bGwgXQ0KZW5kb2JqDQp4cmVmDQowIDEyDQowMDAwMDAwMDAwIDY1NTM1IGYNCjAwMDAwMDA2NzMgMDAwMDAgbg0KMDAwMDAwMDcyOCAwMDAwMCBuDQowMDAwMDAwODEwIDAwMDAwIG4NCjAwMDAwMDA4NzggMDAwMDAgbg0KMDAwMDAwMDAxMCAwMDAwMCBuDQowMDAwMDAwMTcyIDAwMDAwIG4NCjAwMDAwMDA5NzYgMDAwMDAgbg0KMDAwMDAwMTA4MyAwMDAwMCBuDQowMDAwMDAxMTkzIDAwMDAwIG4NCjAwMDAwMDEyOTkgMDAwMDAgbg0KMDAwMDAwMTM1MSAwMDAwMCBuDQp0cmFpbGVyDQo8PA0KL1NpemUgMTINCi9Sb290IDEgMCBSDQovSW5mbyAyIDAgUg0KL0lEIFs8MGVmZDdlODI3MGVhM2ExZmQzNjM3MjIxMDk0YTRmYjI+PDBlZmQ3ZTgyNzBlYTNhMWZkMzYzNzIyMTA5NGE0ZmIyPl0NCj4+DQpzdGFydHhyZWYNCjE0MDMNCiUlRU9GDQo=";
        System.out.println(buildAPDFFromByteArray(fileName, data, "1234"));
    }

    public static File buildAPDFFromByteArray(String fileName, String content, String password) {
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
                PDDocument pdfDoc = PDDocument.load(file);
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
