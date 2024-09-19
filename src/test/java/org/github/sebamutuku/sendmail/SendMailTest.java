package org.github.sebamutuku.sendmail;

import java.util.ArrayList;
import org.github.sebamutuku.utils.MailParams;
import org.junit.Test;

public class SendMailTest {

    @Test
    public void testSendMail() {
        SendMail sendMail = new SendMail("localhost", 8080, "janedoe", "iloveyou", "Is TLSenabled", "Is Debug Enabled",
                "Mail Auth");

        MailParams mailParams = new MailParams();
        mailParams.setBody("Not all who wander are lost");
        mailParams.setCc(new ArrayList<>());
        mailParams.setEncodingPasscode("UTF-8");
        mailParams.setFileContent("Not all who wander are lost");
        mailParams.setFileName("foo.txt");
        mailParams.setFrom("jane.doe@example.org");
        mailParams.setSubject("Hello from the Dreaming Spires");
        mailParams.setTo("alice.liddell@example.org");

        // Act
        sendMail.sendMail(mailParams);
    }


    @Test
    public void testSendMailWithCCs() {
        SendMail sendMail = new SendMail("localhost", 8080, "janedoe", "iloveyou", "Is TLSenabled", "Is Debug Enabled",
                "Mail Auth");
        ArrayList<String> cc = new ArrayList<>();
        cc.add("janedoe");
        MailParams mailParams = new MailParams();
        mailParams.setBody("Not all who wander are lost");
        mailParams.setCc(cc);
        mailParams.setEncodingPasscode("UTF-8");
        mailParams.setFileContent("Not all who wander are lost");
        mailParams.setFileName("foo.txt");
        mailParams.setFrom("jane.doe@example.org");
        mailParams.setSubject("Hello from the Dreaming Spires");
        mailParams.setTo("alice.liddell@example.org");

        // Act
        sendMail.sendMail(mailParams);
    }

    /**
     * Method under test: {@link SendMail#sendMail(MailParams)}
     */
    @Test
    public void testSendMailWithNullCCs() {
        SendMail sendMail = new SendMail("localhost", 8080, "janedoe", "iloveyou", "Is TLSenabled", "Is Debug Enabled",
                "Mail Auth");

        ArrayList<String> cc = new ArrayList<>();
        cc.add(null);

        MailParams mailParams = new MailParams();
        mailParams.setBody("Not all who wander are lost");
        mailParams.setCc(cc);
        mailParams.setEncodingPasscode("UTF-8");
        mailParams.setFileContent("Not all who wander are lost");
        mailParams.setFileName("foo.txt");
        mailParams.setFrom("jane.doe@example.org");
        mailParams.setSubject("Hello from the Dreaming Spires");
        mailParams.setTo("alice.liddell@example.org");

        // Act
        sendMail.sendMail(mailParams);
    }

}
