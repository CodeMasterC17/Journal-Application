package net.engineeringdigest.journalapplication.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    @Disabled
    void sendEmail() {
        emailService.sendEmail("priyanshusingh15101@gmail.com", "Testing java mail sender", "Hi, aap kaise hain ?");
    }
}
