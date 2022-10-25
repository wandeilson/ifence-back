package br.edu.ifpb.dac.groupd.business.service.firebase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.presentation.dto.firebase.PushNotificationRequest;

@Service
public class PushNotificationService {

    private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);
    
    @Autowired
    private FCMService fcmService;

    // public void sendPushNotification(PushNotificationRequest request) {
    //     try {
    //         fcmService.sendMessage(getSamplePayloadData(), request);
    //     } catch (Exception e) {
    //         logger.error(e.getMessage());
    //     }
    // }

    public void sendPushNotificationWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToToken(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToToken(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

//     private Map<String, String> getSamplePayloadData() {
//         Map<String, String> pushData = new HashMap<>();
//         pushData.put("messageId", "msgid");
//         pushData.put("text", "txt");
//         pushData.put("user", "pankaj singh");
//         return pushData;
//     }
}