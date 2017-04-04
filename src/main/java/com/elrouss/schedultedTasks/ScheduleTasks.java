package com.elrouss.schedultedTasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.elrouss.models.FileData;
import com.elrouss.repository.FileDataRepository;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Component
public class ScheduleTasks {
	
	 private MailSender mailSender;
	 private SimpleMailMessage templateMessage;
	 
	 FileDataRepository fileDataRepository;

	 public void setMailSender(MailSender mailSender) {
	        this.mailSender = mailSender;
	 }

	 public void setTemplateMessage(SimpleMailMessage templateMessage) {
	        this.templateMessage = templateMessage;
	 }
	 @Scheduled(fixedRate = 3600)
	 public void placeOrder() {
		 //Retrieve the items in the last hour 
		 Iterable<FileData> files = fileDataRepository.queryLastUploadedFiles();
		 
		 StringBuilder s = new StringBuilder();
		 int i = 0;
		 for(FileData f : files){
			 s.append(i + " - " + f.getName() + "/n" );
			 i++;
		 }
		 // Create a thread safe "copy" of the template message and customize it
	     SimpleMailMessage msg = new SimpleMailMessage();
	        msg.setTo("email recipient");
	        msg.setText("This is the list of all file that were uploaded in the last hour: /n" + s.toString() );
	        try{
	            this.mailSender.send(msg);
	        }
	        catch (MailException ex) {
	            // simply log it and go on...
	            System.err.println(ex.getMessage());
	        }
	    }

}
