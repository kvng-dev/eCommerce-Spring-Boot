package e_commerce.demo.service;

import e_commerce.demo.model.Order;
import e_commerce.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("spring.mail.username")
    private String fromEmail;

    public void sendOrderConfirmation(Order order) {
        // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(order.getUser().getEmail());
        message.setSubject("Order Confirmation");
        message.setText("Your order has been placed successfully. Your order ID is " + order.getId());
        mailSender.send(message);

    }

    public void sendConfirmationCode(User user) {
        // send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Confirm your email");
        message.setText("Please confirm your email by providing this code: " + user.getEmailConfirmationCode());
        mailSender.send(message);
    }
}
