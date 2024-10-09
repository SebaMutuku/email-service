package org.github.sebamutuku.base;

import java.util.List;
import lombok.NonNull;
import org.github.sebamutuku.utils.MailParams;

public abstract class BaseMail {
    public void sendMail(@NonNull MailParams mailParams) {

    }

    public void sendMail(@NonNull String from, String filesDirectory, String recipient, String subject, String body, List<String> cc, boolean deleteFilesAfterSending) {

    }
}
