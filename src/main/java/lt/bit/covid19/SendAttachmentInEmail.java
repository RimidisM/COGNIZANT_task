package lt.bit.covid19;

import java.io.StringWriter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author rimid
 */
public class SendAttachmentInEmail {

    /**
     * Forms email with body and attached file
     * @param dataValue formed XML data
     * @throws TransformerException 
     */
    public static void sendEmail(WriteDataValueModel dataValue) throws TransformerException {

        // Recipient's email ID 
        String to = "recipient@gmail.com";

        // Sender's email ID 
        String from = "sender@gmail.com";

        final String userName = "user name";
        final String password = "password";

        String host = "smtp.gmail.com";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Subject");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText(getEmailBody(dataValue));

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            String fileName = dataValue.fileName;
            messageBodyPart = new MimeBodyPart();
            String file = "c://temp//" + fileName + ".xml";
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Message sent successfully...");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates email body from given data
     * @param dataValue data for email body creation
     * @return string for email body
     * @throws TransformerException 
     */
    private static String getEmailBody(WriteDataValueModel dataValue) throws TransformerException {
        DOMSource source = dataValue.source;
        Transformer transformer = dataValue.transformer;

        //A character stream that collects its output in a string buffer
        StringWriter writer = new StringWriter();
        transformer.transform(source, new StreamResult(writer));

        String xmlString = writer.getBuffer().toString();
        return xmlString;
    }
}
