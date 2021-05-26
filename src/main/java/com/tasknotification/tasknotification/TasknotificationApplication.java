package com.tasknotification.tasknotification;

import com.tasknotification.tasknotification.controller.sendemail.SendEmailThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.scheduling.annotation.*;

@EnableAsync
@SpringBootApplication
public class TasknotificationApplication {
	public static void main(String[] args) {
		SendEmailThread t;

		SpringApplication.run(TasknotificationApplication.class, args);
		t = new SendEmailThread();
		t.start();
	}

}
