package com.onlyflans.bakery;

import com.onlyflans.bakery.model.User;
import com.onlyflans.bakery.model.UserRole;
import com.onlyflans.bakery.persistence.IUserPersistence;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class BakeryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BakeryApplication.class, args);
        System.out.println("[!] OnlyFlans Application is running...");
	}
}
