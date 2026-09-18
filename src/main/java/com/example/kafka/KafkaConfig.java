package com.example.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.kafka.clients.admin.NewTopic;

@ApplicationScoped
public class KafkaConfig {

    @Produces
    public NewTopic notification() {
        return new NewTopic("notification", 1, (short) 1);
    }
}