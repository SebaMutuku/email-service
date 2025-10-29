package org.github.sebamutuku.sendmail;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.NonNull;
import org.github.sebamutuku.base.BaseMail;
import org.github.sebamutuku.utils.FileAttachment;
import org.github.sebamutuku.utils.MailParams;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public final class SendMail extends BaseMail {
    private static final String SMTP_PROTOCOL = "smtp";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";

    private final String emailUsername;
    private final String emailPassword;
    private final boolean authenticationEnabled;
    private final Properties mailProperties;

    public SendMail(String host, int port, String emailUsername, String emailPassword, String isTLSenabled, String isDebugEnabled, String mailAuth, boolean authenticationEnabled) {
        this.emailUsername = Objects.requireNonNull(emailUsername, "Email username cannot be null");
        this.emailPassword = Objects.requireNonNull(emailPassword, "Email password cannot be null");
        this.authenticationEnabled = authenticationEnabled;

        this.mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.host", Objects.requireNonNull(host, "SMTP host cannot be null"));
        mailProperties.setProperty("mail.smtp.port", String.valueOf(port));
        mailProperties.setProperty("mail.smtp.auth", Objects.requireNonNull(mailAuth, "Mail auth cannot be null"));
        mailProperties.setProperty("mail.smtp.starttls.enable", Objects.requireNonNull(isTLSenabled, "TLS setting cannot be null"));
        mailProperties.setProperty("mail.debug", Objects.requireNonNull(isDebugEnabled, "Debug setting cannot be null"));
        mailProperties.setProperty("mail.transport.protocol", SMTP_PROTOCOL);
    }

    @Override
    public void sendMail(@NonNull MailParams mailParams) {
        validateMailParams(mailParams);

        File pdfFile = null;
        try {
            MimeMessage message = createAuthenticatedMessage();
            configureBasicMessageProperties(message, mailParams);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(createTextBodyPart(mailParams.body));

            if (mailParams.fileContent != null && mailParams.fileName != null) {
                pdfFile = handleAttachment(mailParams, multipart);
            }

            message.setContent(multipart);
            addCcRecipients(message, mailParams.cc);
            setReplyToRecipients(message);

            Transport.send(message);
            cleanUpTempFile(pdfFile);

        } catch (MessagingException | IOException e) {
            throw new EmailException("Failed to send email", e);
        }
    }

    @Override
    public void sendMail(@NonNull String from, String filesDirectory, String recipient, String subject, String body, List<String> cc, boolean deleteFilesAfterSending) {
        validateParameters(from, recipient, subject, body);

        try {
            MimeMessage message = createAuthenticatedMessage();
            configureBasicMessageProperties(message, from, recipient, subject);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(createTextBodyPart(body));

            if (filesDirectory != null) {
                handleDirectoryAttachments(filesDirectory, multipart, deleteFilesAfterSending);
            }

            message.setContent(multipart);
            addCcRecipients(message, cc);
            setReplyToRecipients(message);

            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new EmailException("Failed to send email", e);
        }
    }

    private MimeMessage createAuthenticatedMessage() {
        Session session = authenticationEnabled ? Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUsername, emailPassword);
            }
        }) : Session.getInstance(mailProperties);

        return new MimeMessage(session);
    }

    private void configureBasicMessageProperties(MimeMessage message, MailParams params) throws MessagingException {
        String from = params.from.equals(emailUsername) ? emailUsername : params.from;
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(params.to));
        message.setSubject(params.subject);
        message.setSentDate(new Date());
    }

    private void configureBasicMessageProperties(MimeMessage message, String from, String recipient, String subject) throws MessagingException {
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setSentDate(new Date());
    }

    private MimeBodyPart createTextBodyPart(String content) throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        if (content != null) {
            String cleanedContent = content.replace("\n", "").replace("\\n", "");
            String contentType = cleanedContent.contains("html") ? TEXT_HTML : TEXT_PLAIN;
            textPart.setContent(cleanedContent, contentType);
        }
        return textPart;
    }

    private File handleAttachment(MailParams params, Multipart multipart) throws MessagingException, IOException {
        File pdfFile = FileAttachment.createPDFFileFromBase64String(params.fileName, params.fileContent, params.encodingPasscode);

        if (pdfFile.exists()) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(pdfFile);
            multipart.addBodyPart(attachmentPart);
            System.out.println("Attached file: " + pdfFile.getName());
        }
        return pdfFile;
    }

    private void handleDirectoryAttachments(String directoryPath, Multipart multipart, boolean deleteAfter) throws MessagingException, IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    attachFile(multipart, file);
                    if (deleteAfter) {
                        deleteFileAfterSending(file);
                    }
                }
            }
        }
    }

    private void attachFile(Multipart multipart, File file) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
        attachmentPart.setFileName(file.getName());
        multipart.addBodyPart(attachmentPart);
        System.out.println("Attached file: " + file.getName());
    }

    private void addCcRecipients(MimeMessage message, List<String> ccAddresses) throws MessagingException {
        if (ccAddresses != null && !ccAddresses.isEmpty()) {
            InternetAddress[] addresses = ccAddresses.stream().filter(Objects::nonNull).map(email -> {
                try {
                    return new InternetAddress(email.trim());
                } catch (AddressException e) {
                    throw new EmailAddressException("Invalid CC email address: " + email, e);
                }
            }).toArray(InternetAddress[]::new);

            if (addresses.length > 0) {
                message.addRecipients(Message.RecipientType.CC, addresses);
            }
        }
    }

    private void setReplyToRecipients(MimeMessage message) throws MessagingException {
        Address[] recipients = message.getAllRecipients();
        if (recipients != null && recipients.length > 0) {
            message.setReplyTo(recipients);
        }
    }

    private void cleanUpTempFile(File file) {
        if (file != null && file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted temporary file: " + file.getName());
            } else {
                System.err.println("Failed to delete temporary file: " + file.getName());
            }
        }
    }

    private void deleteFileAfterSending(File file) {
        if (file.delete()) {
            System.out.println("Deleted file after sending: " + file.getName());
        } else {
            System.err.println("Failed to delete file after sending: " + file.getName());
        }
    }

    private void validateMailParams(MailParams params) {
        Objects.requireNonNull(params, "MailParams cannot be null");
        Objects.requireNonNull(params.to, "Recipient address cannot be null");
        Objects.requireNonNull(params.subject, "Email subject cannot be null");

        if (params.to.trim().isEmpty()) {
            throw new IllegalArgumentException("Recipient address cannot be empty");
        }
    }

    private void validateParameters(String from, String recipient, String subject, String body) {
        Objects.requireNonNull(from, "From address cannot be null");
        Objects.requireNonNull(recipient, "Recipient address cannot be null");
        Objects.requireNonNull(subject, "Email subject cannot be null");

        if (from.trim().isEmpty() || recipient.trim().isEmpty()) {
            throw new IllegalArgumentException("Email addresses cannot be empty");
        }
    }

    // Custom exception classes
    public static class EmailException extends RuntimeException {
        public EmailException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class EmailAddressException extends RuntimeException {
        public EmailAddressException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}