package com.kcbgroup.sendmail;


import com.kcbgroup.utils.FileAttachment;
import com.kcbgroup.utils.MailParams;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.NonNull;


public class SendMail {
    private final String emailUsername;
    private final String emailPassword;
    Properties props;

    public SendMail(String host, int port, String emailUsername, String emailPassword, String isTLSenabled, String isDebugEnabled, String mailAuth) {
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", props.getOrDefault(mailAuth, "false"));
        props.put("mail.smtp.starttls.enable", props.getOrDefault(isTLSenabled, "false"));
        props.put("mail.debug", props.getOrDefault(isDebugEnabled, "false"));
    }

    public static void main(String[] args) {
        String host = "smtp.office365.com";
        String mailPassword = "$7+pw2Og4P!wfbR";
        String mailuserName = "mmailer@kcbgroup.com";
        int port = 587;
        MailParams params = new MailParams();
        params.setEncodingPasscode("1234");
        params.subject = "What's up";
        params.from = mailuserName;
        params.setBody("You are working today?");
        params.fileContent = "JVBERi0xLjQNCjUgMCBvYmoNCjw8DQovVHlwZSAvUGFnZQ0KL1BhcmVudCAzIDAgUg0KL1Jlc291cmNlcyA0IDAgUg0KL0NvbnRlbnRzIDYgMCBSDQovTWVkaWFCb3hbIDAgMCA1OTUuMzUgODQxLjk1IF0NCi9Dcm9wQm94WyAwIDAgNTk1LjM1IDg0MS45NSBdDQovUm90YXRlIDANCj4+DQplbmRvYmoNCjYgMCBvYmoNCjw8IC9MZW5ndGggNDIzIC9GaWx0ZXIgL0ZsYXRlRGVjb2RlID4+DQpzdHJlYW0NCniclZTbTsJAEIbvm/Qd5hJvxp097y2KhISAwb4AalUMSFIxvo6P6m67wlag2vSih5nZ7/9nBhgwfy3G9Y1B9ZxnwyLPqH4jEBqMEGiUg2KTZ5c3lGdADIqnPBt8XRSvh1QuDTKmwJBDJ2zM5yGfN/nDyXQ6mY1hMbqdL4q6eORZaI2Gzzw7HGBRaw0bENKikIcva7gLvHB5xc1DUCyUVwiGCVRer0ML5B+hKsFj2d8OmcG9YJEYnFePZQWzj819WUWvR+zggEih2guwpgc+lNpUwWBYLd8eXmC23JRdTM54yiQUqgc2VLexjdfr5a6bagW6tNO9oFYiqRPUYtXtVWiRDJi8dtcDK8mc9Hq1fd91UaXWqPdUZ7GPV8UVSkXtya7W65oKnWbrZdaOITte5rhpMdretLgRP7HfG9GMLjm3jx0hJSpGvlwhp/TPwDU/lWK7W67/McPIP5phbHYM/6PZoROiHo4gCNky5vr+qVPflZbBv/XO6wA3rdYE+KlY0H0mFDTLMyd6fx3qSIcSxX2lc3BgJdJDMAm1JXI/Xd/MffQb0XQnBA0KZW5kc3RyZWFtDQplbmRvYmoNCjEgMCBvYmoNCjw8DQovVHlwZSAvQ2F0YWxvZw0KL1BhZ2VzIDMgMCBSDQo+Pg0KZW5kb2JqDQoyIDAgb2JqDQo8PA0KL1R5cGUgL0luZm8NCi9Qcm9kdWNlciAoT3JhY2xlIEJJIFB1Ymxpc2hlciAxMC4xLjMuNC4xKQ0KPj4NCmVuZG9iag0KMyAwIG9iag0KPDwNCi9UeXBlIC9QYWdlcw0KL0tpZHMgWw0KNSAwIFINCl0NCi9Db3VudCAxDQo+Pg0KZW5kb2JqDQo0IDAgb2JqDQo8PA0KL1Byb2NTZXQgWyAvUERGIC9UZXh0IF0NCi9Gb250IDw8IA0KL0YxIDcgMCBSDQovRjIgOCAwIFINCi9GMyA5IDAgUg0KPj4NCj4+DQplbmRvYmoNCjcgMCBvYmoNCjw8DQovVHlwZSAvRm9udA0KL1N1YnR5cGUgL1R5cGUxDQovQmFzZUZvbnQgL1RpbWVzLVJvbWFuDQovRW5jb2RpbmcgL1dpbkFuc2lFbmNvZGluZw0KPj4NCmVuZG9iag0KOCAwIG9iag0KPDwNCi9UeXBlIC9Gb250DQovU3VidHlwZSAvVHlwZTENCi9CYXNlRm9udCAvSGVsdmV0aWNhLUJvbGQNCi9FbmNvZGluZyAvV2luQW5zaUVuY29kaW5nDQo+Pg0KZW5kb2JqDQo5IDAgb2JqDQo8PA0KL1R5cGUgL0ZvbnQNCi9TdWJ0eXBlIC9UeXBlMQ0KL0Jhc2VGb250IC9UaW1lcy1Cb2xkDQovRW5jb2RpbmcgL1dpbkFuc2lFbmNvZGluZw0KPj4NCmVuZG9iag0KMTAgMCBvYmoNClsgNSAwIFIgL1hZWiAzNi4wIDY3Ny4wOTQgbnVsbCBdDQplbmRvYmoNCjExIDAgb2JqDQpbIDUgMCBSIC9YWVogMzYuMCA2NzcuMDk0IG51bGwgXQ0KZW5kb2JqDQp4cmVmDQowIDEyDQowMDAwMDAwMDAwIDY1NTM1IGYNCjAwMDAwMDA2NzMgMDAwMDAgbg0KMDAwMDAwMDcyOCAwMDAwMCBuDQowMDAwMDAwODEwIDAwMDAwIG4NCjAwMDAwMDA4NzggMDAwMDAgbg0KMDAwMDAwMDAxMCAwMDAwMCBuDQowMDAwMDAwMTcyIDAwMDAwIG4NCjAwMDAwMDA5NzYgMDAwMDAgbg0KMDAwMDAwMTA4MyAwMDAwMCBuDQowMDAwMDAxMTkzIDAwMDAwIG4NCjAwMDAwMDEyOTkgMDAwMDAgbg0KMDAwMDAwMTM1MSAwMDAwMCBuDQp0cmFpbGVyDQo8PA0KL1NpemUgMTINCi9Sb290IDEgMCBSDQovSW5mbyAyIDAgUg0KL0lEIFs8MGVmZDdlODI3MGVhM2ExZmQzNjM3MjIxMDk0YTRmYjI+PDBlZmQ3ZTgyNzBlYTNhMWZkMzYzNzIyMTA5NGE0ZmIyPl0NCj4+DQpzdGFydHhyZWYNCjE0MDMNCiUlRU9GDQo=";
        params.fileName = "abc_test.pdf";
        params.to = "seb.mutuku@gmail.com";
        SendMail sendMail = new SendMail(host, port, mailuserName, mailPassword, "true", "true", "true");
        sendMail.sendMail(params);

    }

    public void sendMail(@NonNull MailParams mailParams) {
        MimeMessage message;
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailUsername, emailPassword);
                }
            });
            message = new MimeMessage(session);
            if (emailUsername.equals(mailParams.from)) {
                mailParams.from = emailUsername;
            }

            message.setFrom(new InternetAddress(mailParams.from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailParams.to));
            message.setSubject(mailParams.subject);
            message.setText(mailParams.body);
            message.setSentDate(new Date());

            if (mailParams.fileContent != null && mailParams.fileName != null) {

                File file = FileAttachment.buildAPDFFromByteArray(mailParams.fileName, mailParams.fileContent, mailParams.encodingPasscode);
                Multipart multipart = new MimeMultipart();
                if (file.isFile() && file.exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(file);
                    multipart.addBodyPart(attachmentPart);
                    System.out.println("Attaching file [" + file.getName() + "]");
                }
                message.setContent(multipart);
            }
            if (mailParams.cc != null && !mailParams.cc.isEmpty()) {
                final Address[] addresses = new InternetAddress[mailParams.cc.size()];
                for (int i = 0; i < addresses.length; i++) {
                    addresses[i] = new InternetAddress(mailParams.cc.get(i));

                }
                message.addRecipients(Message.RecipientType.CC, addresses);

            }
            Address[] address = message.getAllRecipients();
            message.setReplyTo(address);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
