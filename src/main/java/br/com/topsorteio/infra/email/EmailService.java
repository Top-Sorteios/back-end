package br.com.topsorteio.infra.email;


import br.com.topsorteio.dtos.EmailSenderDTO;
import br.com.topsorteio.exceptions.EventInternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailSenderDTO email){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreplay@maisfocounisolutions.com.br");
            message.setTo(email.para());
            message.setSubject(email.assunto());
            message.setText(email.mensagem());

            mailSender.send(message);

        }catch(Exception ex){
            throw new EventInternalServerErrorException(ex.getMessage());
        }

    }
}
