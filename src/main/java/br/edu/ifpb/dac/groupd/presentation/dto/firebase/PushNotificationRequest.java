package br.edu.ifpb.dac.groupd.presentation.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;

    @Override
    public String toString() {
        return "PushNotificationRequest{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", topic='" + topic + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}