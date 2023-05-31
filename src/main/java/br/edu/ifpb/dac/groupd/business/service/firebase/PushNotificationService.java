package br.edu.ifpb.dac.groupd.business.service.firebase;

import br.edu.ifpb.dac.groupd.presentation.controller.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.presentation.dto.firebase.PushNotificationRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushNotificationService {
    
    @Autowired
    private FCMService fcmService;

    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}