# Email Service Maven Plugin

![Maven Central](https://img.shields.io/maven-central/v/io.github.sebamutuku/email-service?color=blue)
![License](https://img.shields.io/badge/license-Apache%202.0-green)

A Maven plugin for sending emails with PDF attachments generated from byte arrays.

## Features

- Send emails with attachments via SMTP
- Generate PDF files from Base64 encoded content
- Password-protect PDF attachments
- Attach multiple files from a directory
- Configurable SMTP settings
- Automatic cleanup of temporary files

## Installation

Add the plugin to your project's `pom.xml`:

```xml

<build>
    <plugins>
        <plugin>
            <groupId>io.github.sebamutuku</groupId>
            <artifactId>email-service</artifactId>
            <version>${version}</version>
        </plugin>
    </plugins>
</build>
```
# Usage
## Basic Configuration

```xml

<configuration>
    <host>smtp.example.com</host>
    <port>587</port>
    <username>your-email@example.com</username>
    <password>your-password</password>
    <tlsEnabled>true</tlsEnabled>
    <debugEnabled>false</debugEnabled>
    <authEnabled>true</authEnabled>
</configuration>
```

## Sending Emails

```java
import io.github.sebamutuku.emailservice.SendMail;
import io.github.sebamutuku.emailservice.MailParams;
import java.util.Arrays;
MailParams params = new MailParams();
params.setFrom("sender@example.com");
params.setTo("recipient@example.com");
params.setSubject("Test Email");
params.setBody("Email content");
params.setFileName("document.pdf");
params.setFileContent("Base64EncodedPDFContent");
params.setEncodingPasscode("pdfpassword");
params.setCc(Arrays.asList("cc1@example.com", "cc2@example.com"));

SendMail sendMail = new SendMail(
    "smtp.example.com", 587, 
    "username", "password", 
    "true", "false", "true", true);
sendMail.sendMail(params);

```

## Building
To build the project, run:

```bash
mvn clean install
```
# License
## Apache 2.0
To create the file:

1. Copy all the text above
2. Create a new file named `README.md`
3. Paste the content
4. Save the file

The file is now ready to use with your project. The formatting will render properly on GitHub or any other Markdown viewer.