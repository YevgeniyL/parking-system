package components;

import com.parkingsystem.domain.model.email.EmailMessageEntity;
import com.parkingsystem.domain.model.parking.EmailMessage;
import com.parkingsystem.domain.repository.SessionRepository;
import com.parkingsystem.domain.sevice.EmailService;
import com.parkingsystem.infrastructure.repository.EmailRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendAllNotSendedMessage() {
        try {
            log.info("Start send all not sended email messages");
            List<EmailMessageEntity> unsendedMessages = emailRepository.findBySendedIsNull();
            for (EmailMessageEntity emailMessageEntity : unsendedMessages) {
                sendEmail(emailMessageEntity);
                emailMessageEntity.setSended(LocalDateTime.now());
                emailRepository.save(emailMessageEntity);
            }
        } catch (Exception e){
            log.error("Unsended messages not sended", e);
        }
    }

    @Override
    public void sendMessage(EmailMessage message) {
        EmailMessageEntity emailMessage = new EmailMessageEntity();
        emailMessage.setSendTo(message.getSendTo());
        emailMessage.setSubject(message.getSubject());
        emailMessage.setSession(sessionRepository.find(message.getSessionId()).get()); //it is forever is present
        emailMessage.setCreated(LocalDateTime.now());
        emailMessage.setText(message.getText());
        emailRepository.save(emailMessage);
        sendEmail(emailMessage);
        emailMessage.setSended(LocalDateTime.now());
        emailRepository.save(emailMessage);
    }

    public void sendEmail(EmailMessageEntity emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailMessage.getSendTo());
        message.setSubject(emailMessage.getSubject());
        message.setText(emailMessage.getText());
        message.setFrom(from);
        emailSender.send(message);
    }
}
