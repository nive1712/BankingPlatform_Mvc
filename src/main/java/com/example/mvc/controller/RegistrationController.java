package com.example.mvc.controller;

import com.example.mvc.model.BankAccount;

import com.example.mvc.model.User;
import com.example.mvc.service.PlatformService;
import com.example.mvc.imple.BankAccountDaoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
public class RegistrationController {
	private static final Logger logger = LogManager.getLogger(RegistrationController.class);

	private static final String MESSAGE = "message";
	private final PasswordEncoder passwordencoder; 
	private final PlatformService platformService;
	private final BankAccountDaoImpl bankAccountDao;

	@Autowired
	public RegistrationController(PasswordEncoder passwordencoder, PlatformService platformService,
			BankAccountDaoImpl bankAccountDao) {
		this.passwordencoder = passwordencoder; 
		this.platformService = platformService;
		this.bankAccountDao = bankAccountDao;
	}

	private SecureRandom secureRandom = new SecureRandom();

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user, String password, Model model) {

		if (isValidInput(user, password)) {

			user.setRole("USER");
			user.setPassword(passwordencoder.encode(user.getPassword()));
			int userId = platformService.registerUser(user, password);

			if (userId != -1) {
				user.setRole("USER");
				user.setUserId(userId);
				user.setPassword(passwordencoder.encode(password));

				createBankAccount(user);
				model.addAttribute(MESSAGE, "User registered successfully with userId: " + userId);
				model.addAttribute("accountNumber", user.getBankAccounts().iterator().next().getAccountNumber());
			} else {
				model.addAttribute(MESSAGE, "User registration failed.");
			}
		} else {
			model.addAttribute(MESSAGE, "Invalid input. Please follow the input guidelines.");
		}

		return "registrationResult";
	}

	private boolean isValidInput(User user, String password) {
		return user.getName().matches("^[A-Z].*") && user.getPhoneNumber().matches("\\d{10}")
				&& user.getEmail().endsWith("@gmail.com") && password.length() >= 8 && password.matches(".*[A-Z].*")
				&& password.matches(".*[a-z].*") && password.matches(".*\\d.*")
				&& password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
	}

	private void createBankAccount(User user) {

		BankAccount bankAccount = new BankAccount();
		bankAccount.setUser(user);
		bankAccount.setAccountNumber(bankAccountDao.generateAccountNumber());
		bankAccount.setPin(secureRandom.nextInt(10000));
		bankAccount.setMfaPin(1111 + user.getUserId());
		bankAccount.setBalance(BigDecimal.valueOf(500));
		bankAccount.setInitialBalance(BigDecimal.valueOf(500));

		if (bankAccountDao.createBankAccount(bankAccount)) {
			if (user.getBankAccounts() == null) {
				user.setBankAccounts(new HashSet<>());
			}
			Set<BankAccount> bankAccounts = new HashSet<>(user.getBankAccounts());
			bankAccounts.add(bankAccount);
			user.setBankAccounts(bankAccounts);
		} else {
			logger.warn("Failed to create bank account for user ID: {}", user.getUserId());
		}
	}
}
