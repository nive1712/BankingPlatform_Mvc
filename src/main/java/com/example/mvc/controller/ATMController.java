package com.example.mvc.controller;


import com.example.mvc.model.*;
import com.example.mvc.service.PlatformService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/atm")
public class ATMController {
	private static final String ERROR = "error";
	private static final String DEPOSIT_ACTION = "deposit";
	private static final String WITHDRAW = "withdraw";
	private static final String REDIRECT_TRANSACTION_HISTORY = "redirect:/transactionHistory";

	    private final PlatformService platformService;

	    @Autowired
	    public ATMController(PlatformService platformService) {
	       
	        this.platformService = platformService;
	    }

	@GetMapping("/deposit")
	public String showDepositPage(Model model) {
		return DEPOSIT_ACTION;
	}

	@PostMapping("/deposit")
	public String handleDeposit(@RequestParam("amount") double amount, Principal principal, Model model) {

		User user = platformService.getUserByEmail(principal.getName());
		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		boolean cardBlocked = platformService.isCardBlocked(accountNumber);// to check card has been blocked or not

		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Deposit not allowed.");
			return DEPOSIT_ACTION;
		}

		boolean depositSuccess = platformService.deposit(user.getUserId(), amount);

		if (depositSuccess) {
			return REDIRECT_TRANSACTION_HISTORY;
		} else {
			model.addAttribute(ERROR, "Deposit failed. Please try again.");
			return DEPOSIT_ACTION;
		}
	}

	@GetMapping("/withdraw")
	public String showWithdrawPage(Model model) {
		return WITHDRAW;
	}

	@PostMapping("/withdraw")
	public String handleWithdraw(@RequestParam("amount") double amount, Principal principal, Model model) {

		User user = platformService.getUserByEmail(principal.getName());

		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		boolean cardBlocked = platformService.isCardBlocked(accountNumber);// to check card has been blocked or not

		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Withdraw not allowed.");
			return WITHDRAW;
		}
		boolean withdrawSuccess = platformService.withdraw(user.getUserId(), amount);

		if (withdrawSuccess) {
			return REDIRECT_TRANSACTION_HISTORY;
		} else {
			model.addAttribute(ERROR, "Withdrawal failed. Please try again.");
			return WITHDRAW;
		}
	}

	@GetMapping("/transfer")
	public String showTransferPage(Model model) {
		return "transfer";
	}

	@PostMapping("/transfer")
	public String handleTransfer(@RequestParam("recipientAccountNumber") String recipientAccountNumber,
			@RequestParam("amount") BigDecimal amount, Principal principal, Model model) {
		User user = platformService.getUserByEmail(principal.getName());
		String senderAccountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		boolean cardBlocked = platformService.isCardBlocked(senderAccountNumber);//// to check card has been blocked or
																					//// not

		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Transfer not allowed.");
			return "transfer";
		}

		platformService.transfer(user.getUserId(), senderAccountNumber, recipientAccountNumber, amount);
		return REDIRECT_TRANSACTION_HISTORY;
	}

}
