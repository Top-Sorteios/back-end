package br.com.topsorteio.infra.email;


import br.com.topsorteio.dtos.EmailSenderDTO;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    public void sendEmail(EmailSenderDTO emailDto) {

        Email from = new Email("tecnologia@maisfocounisolutions.com.br");
        String subject = emailDto.assunto();
        Email to = new Email(emailDto.para());
        Content content = new Content("text/plain", emailDto.mensagem());
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);

    }
}