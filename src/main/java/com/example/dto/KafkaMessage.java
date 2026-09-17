package com.example.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessage {

    private String tipoNotifica;
    private Map<String, String> properties = new HashMap<>();
}
