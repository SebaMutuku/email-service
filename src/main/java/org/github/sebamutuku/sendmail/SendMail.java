package org.github.sebamutuku.sendmail;


import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.github.sebamutuku.utils.FileAttachment;
import org.github.sebamutuku.utils.MailParams;

public class SendMail {
    private final String emailUsername;
    private final String emailPassword;
    private final boolean authenticationEnabled;
    Properties props;

    public SendMail(String host, int port, String emailUsername, String emailPassword, String isTLSenabled, String isDebugEnabled, String mailAuth, boolean authenticationEnabled) {
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        this.authenticationEnabled = authenticationEnabled;
        props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", String.valueOf(port));
        props.setProperty("mail.smtp.auth", mailAuth);
        props.setProperty("mail.smtp.starttls.enable", isTLSenabled);
        props.setProperty("mail.debug", isDebugEnabled);
        props.setProperty("mail.transport.protocol", "smtp");
    }


    private static MimeBodyPart addMessageBody(String mailParams) throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        if (mailParams != null) {
            mailParams = mailParams.replace("\n", "").replace("\\n", "");
            if (mailParams.contains("html")) {
                textPart.setContent(mailParams, "text/html");
            } else textPart.setContent(mailParams, "text/plain");

        }
        return textPart;
    }

    private static void appendCcs(List<String> mailParams, MimeMessage message) throws MessagingException {
        if (mailParams != null && !mailParams.isEmpty()) {
            InternetAddress[] addresses = mailParams.stream().map(email -> {
                try {
                    return new InternetAddress(email);
                } catch (AddressException e) {
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).toArray(InternetAddress[]::new);
            message.addRecipients(Message.RecipientType.CC, addresses);
        }
    }

    public void sendMail(@NonNull MailParams mailParams) {
        MimeMessage message;
        File pdfFile = null;
        try {
            message = authenticateViaMime();
            if (emailUsername.equals(mailParams.from)) {
                mailParams.from = emailUsername;
            }
            message.setFrom(new InternetAddress(mailParams.from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailParams.to));
            message.setSubject(mailParams.subject);
            message.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            // Add the email body
            MimeBodyPart textPart = addMessageBody(mailParams.body);

            multipart.addBodyPart(textPart);
            if (mailParams.fileContent != null && mailParams.fileName != null) {
                pdfFile = FileAttachment.createPDFFileFromBase64String(mailParams.fileName, mailParams.fileContent, mailParams.encodingPasscode);
                if (pdfFile.isFile() && pdfFile.exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(pdfFile);
                    multipart.addBodyPart(attachmentPart);
                    System.out.println("Attaching file [" + pdfFile.getName() + "]");
                }
                message.setContent(multipart);
            }
            appendCcs(mailParams.cc, message);
            Address[] address = message.getAllRecipients();
            message.setReplyTo(address);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete();
                System.out.println("Deleting file [" + pdfFile.getName() + "]");
            }
        }

    }

    @SneakyThrows
    public void sendMail(@NonNull String from, String filesDirectory, String recipient, String subject, String body, List<String> cc, boolean deleteFilesAfterSending) {
        MimeMessage message;
        try {
            message = authenticateViaMime();
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setSentDate(new Date());
            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(addMessageBody(body));
            if (filesDirectory != null && !filesDirectory.isEmpty()) {
                File directory = new File(filesDirectory);
                File[] files = directory.listFiles();
                if (files != null) {
                    Arrays.stream(files).filter(File::isFile).forEach(file -> {
                        try {
                            MimeBodyPart attachmentPart = new MimeBodyPart();
                            attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
                            attachmentPart.setFileName(file.getName());
                            multipart.addBodyPart(attachmentPart);
                            System.out.println("Attaching file [" + file.getName() + "]");
                        } catch (MessagingException e) {
                            System.err.println("Failed to attach file: " + file.getName());
                        }
                    });
                }
                message.setContent(multipart);
            }

            appendCcs(cc, message);
            Address[] address = message.getAllRecipients();
            message.setReplyTo(address);
            Transport.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        } finally {
            if (deleteFilesAfterSending && filesDirectory != null && !filesDirectory.isEmpty()) {
                File directory = new File(filesDirectory);
                File[] files = directory.listFiles();
                if (files != null) {
                    Arrays.stream(files).filter(File::isFile).forEach(file -> {
                        file.delete();
                        System.out.println("Deleting file [" + file.getName() + "]");
                    });
                }
            }
        }

    }


    private MimeMessage authenticateViaMime() {
        MimeMessage message;
        Session session;
        if (this.authenticationEnabled) {
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailUsername, emailPassword);
                }
            });
        } else {
            session = Session.getInstance(props, new Authenticator() {
            });
        }
        message = new MimeMessage(session);
        return message;
    }

}
