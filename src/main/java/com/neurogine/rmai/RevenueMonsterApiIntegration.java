package com.neurogine.rmai;

import com.neurogine.rmai.service.TopupRequestGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class RevenueMonsterApiIntegration implements CommandLineRunner {

    private final ApplicationContext context;

    public RevenueMonsterApiIntegration(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(RevenueMonsterApiIntegration.class, args);
    }

    @Override
    public void run(String... args) {
        MessagingTemplate template = new MessagingTemplate();
        String response = template.convertSendAndReceive(context.getBean("requestChannel", MessageChannel.class),
                TopupRequestGenerator.generateRequest(), String.class);
        System.out.println("Response: " + response);
    }
}