package org.github.sebamutuku.utils;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class MailParams {
    /**
     * Sender email address
     */
    public String from;
    /**
     * Recipient email address
     */
    public String to;
    /**
     * Email subject line
     */
    public String subject;
    /**
     * Email message body content
     */
    public String body;
    /**
     * CC recipients
     */
    public List<String> cc;
    /**
     * Passcode for encoding/decoding
     */
    public String encodingPasscode;
    /**
     * Attached file content (Base64 or raw)
     */
    public String fileContent;
    /**
     * Name of the attached file
     */
    public String fileName;

}
