package com.project.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.project.dto.EmailDto;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	public void send(EmailDto email) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, email.isMultipart(), email.getEncoding());
		
		mimeMessage.setContent(email.getContent(), email.getMessageType());
		helper.setTo(email.getSendTo());
		helper.setSubject("Acount activation:");
		helper.setFrom(email.getSendFrom());
//		mailSender.send(mimeMessage);
		System.out.println("saljem mejl");	
	}
}
