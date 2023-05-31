package br.edu.ifpb.dac.groupd.presentation.controller;

import br.edu.ifpb.dac.groupd.presentation.dto.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    public String sendEmail(@RequestBody EmailDto emailDto){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailDto.getTo());
        simpleMailMessage.setSubject(emailDto.getSubject());
        simpleMailMessage.setText(emailDto.getText());

        javaMailSender.send(simpleMailMessage);

        System.out.println("Email enviado.");
        return "Email send successfully";
    }
}
