package org.github.sebamutuku.sendmail;


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
import org.github.sebamutuku.utils.FileAttachment;
import org.github.sebamutuku.utils.MailParams;


public class SendMail {
    private final String emailUsername;
    private final String emailPassword;
    Properties props;

    public SendMail(String host, int port, String emailUsername, String emailPassword, String isTLSenabled, String isDebugEnabled, String mailAuth) {
        this.emailUsername = emailUsername;
        this.emailPassword = emailPassword;
        props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", String.valueOf(port));
        props.setProperty("mail.smtp.auth", mailAuth);
        props.setProperty("mail.smtp.starttls.enable", isTLSenabled);
        props.setProperty("mail.debug", isDebugEnabled);
        props.setProperty("mail.transport.protocol", "smtp");
    }

    public void sendMail(@NonNull MailParams mailParams) {
        MimeMessage message;
        File pdfFile = null;
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

                pdfFile = FileAttachment.createPDFFileFromBase64String(mailParams.fileName, mailParams.fileContent, mailParams.encodingPasscode);
                Multipart multipart = new MimeMultipart();
                if (pdfFile.isFile() && pdfFile.exists()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(pdfFile);
                    multipart.addBodyPart(attachmentPart);
                    System.out.println("Attaching pdfFile [" + pdfFile.getName() + "]");
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
        } finally {
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete();
                System.out.println("Deleting pdfFile [" + pdfFile.getName() + "]");
            }
        }

    }
}
