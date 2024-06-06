package br.com.topsorteio.infra.email;


import br.com.topsorteio.dtos.EmailSenderDTO;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import lombok.AllArgsConstructor;
import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailSenderDTO email){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@topsorteio.com");
            message.setTo(email.para());
            message.setSubject(email.assunto());
            message.setText(email.mensagem());

            mailSender.send(message);

        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
}
