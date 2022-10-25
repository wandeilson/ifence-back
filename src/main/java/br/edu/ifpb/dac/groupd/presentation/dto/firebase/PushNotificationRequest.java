package br.edu.ifpb.dac.groupd.presentation.dto.firebase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;  
}