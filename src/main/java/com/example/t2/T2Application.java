package com.example.t2;

import com.example.t2.entities.PasswordRefactor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class T2Application {

	public static void main(String[] args) {
		SpringApplication.run(T2Application.class, args);
	}
	Scanner leer=new Scanner(System.in);
	PasswordRefactor passwordRefactor=new PasswordRefactor();
	String pw1="147369";
	String salida2=passwordRefactor.encryptPassword(pw1);
	String salida3=passwordRefactor.decryptPassword(salida2);


}
