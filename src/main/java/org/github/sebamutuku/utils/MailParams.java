package org.github.sebamutuku.utils;

import java.util.List;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class MailParams {
    public String from;
    public String to;
    public String subject;
    public String body;
    public List<String> cc;
    public String encodingPasscode;
    public String fileContent;
    public String fileName;

}
