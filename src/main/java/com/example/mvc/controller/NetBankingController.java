package com.example.mvc.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.mvc.model.BankAccount;
import com.example.mvc.model.Budget;
import com.example.mvc.model.CardBlockStatus;
import com.example.mvc.model.Loan;
import com.example.mvc.model.User;
import com.example.mvc.service.PlatformService;

@Controller
@RequestMapping("/netbanking")
public class NetBankingController {
	private static final Logger logger = LogManager.getLogger(NetBankingController.class);
	private static final String ERROR = "error";
	private static final String LOAN_HISTORY = "loanHistory";
	private static final String SUCCESS = "success";
	private static final String USER_NOT_FOUND_MESSAGE = "User not found.";
	private static final String PAY_LOAN_ACTION = "payLoan";
	private static final String DEPOSIT_ACTION = "deposit";

	private static final String LOAN_APPLICATION_ACTION = "loanApplication";
	private static final String TRANSFER_ACTION = "transfer";

	private final PlatformService platformService;
	

	@Autowired
	public NetBankingController(PlatformService platformService) {
		this.platformService = platformService;

	}

	@GetMapping("/deposit")
	public String showDepositPage(Model model) {
		return DEPOSIT_ACTION;
	}

	@PostMapping("/completedeposit")
	public String handleDeposit(@RequestParam("amount") BigDecimal amount,
			@AuthenticationPrincipal UserDetails userDetails, Model model) {
		String email = userDetails.getUsername();
		logger.info("User email from UserDetails:{} ", email);
		User user = platformService.getUserByEmail(email);
		if (user == null) {
			logger.info("User not found for email:{} ", email);
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return DEPOSIT_ACTION;
		}

		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());
		boolean cardBlocked = platformService.isCardBlocked(accountNumber);
		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Deposit cannot be processed.");
			return DEPOSIT_ACTION;
		}

		platformService.handleDeposit(user.getUserId(), amount);

		model.addAttribute(SUCCESS, "Deposit successful!");

		return "redirect:/transactionHistory";
	}

	@GetMapping("/transfer")
	public String showTransferPage(Model model) {
		return TRANSFER_ACTION;
	}

	@PostMapping("/transfer")
	public String handleTransfer(@RequestParam("senderAccountNumber") String senderAccountNumber,
			@RequestParam("recipientAccountNumber") String recipientAccountNumber,
			@RequestParam("amount") BigDecimal amount, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		if (user == null) {
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return TRANSFER_ACTION;
		}

		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		boolean cardBlocked = platformService.isCardBlocked(accountNumber);
		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Transfer cannot be processed.");
			return TRANSFER_ACTION;
		}
		platformService.handleTransfer(senderAccountNumber, recipientAccountNumber, amount);

		model.addAttribute(SUCCESS, "Transfer successful!");

		return "redirect:/transactionHistory";
	}

	@GetMapping("/block")
	public String showBlockCardForm(Model model) {
		model.addAttribute("cardBlockStatus", new CardBlockStatus());
		return "blockCard";
	}

	@PostMapping("/blockCard")
	public String handleBlockCard(@RequestParam("accountNumber") String accountNumber, @RequestParam("pin") int pin,
			@RequestParam("reason") String reason, @AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		if (user == null) {
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return "blockCard";
		}

		boolean success = platformService.blockCard(user.getUserId(), accountNumber, pin, reason);

		if (success) {
			model.addAttribute(SUCCESS, "Card successfully blocked.");
			model.addAttribute("accountNumber", accountNumber);
			model.addAttribute("reason", reason);
			model.addAttribute("message", "Your card has been blocked due to: " + reason);
		} else {

			model.addAttribute(ERROR, "Failed to block the card.");
		}

		return "blockCardDetails";
	}

	@GetMapping("/unblock")
	public String showUnblockCardForm(Model model) {
		model.addAttribute("cardBlockStatus", new CardBlockStatus());
		return "unblockCard";
	}

	@PostMapping("/unblockCard")
	public String unblockCard(@RequestParam("accountNumber") String accountNumber, @RequestParam("pin") int pin,
			@AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		if (user == null) {
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return "unblockCard";
		}

		boolean success = platformService.unblockCard(user.getUserId(), accountNumber, pin);

		if (success) {
			model.addAttribute("accountNumber", accountNumber);
			model.addAttribute("status", "Card successfully unblocked.");
		} else {
			model.addAttribute("status", "Failed to unblock the card.");
		}

		return "unblockCardDetails";
	}

	@GetMapping("/calculator")
	public String showBudgetCalculatorForm(Model model) {
		model.addAttribute("budget", new Budget());
		return "budgetCalculator";
	}

	@PostMapping("/calculate")
	public String calculateBudget(@ModelAttribute("budget") Budget budget, Model model) {
		Budget calculatedBudget = platformService.calculateBudget(budget);
		model.addAttribute("calculatedBudget", calculatedBudget);
		return "budgetResult";

	}

	@GetMapping("/loanApplication")
	public String showLoanApplicationForm(Model model) {
		model.addAttribute("loan", new Loan());
		return LOAN_APPLICATION_ACTION;
	}

	@PostMapping("/applyLoan")
	public String applyForLoan(@RequestParam("bankAccountId") int bankAccountId,
			@RequestParam("totalPaymentAmount") BigDecimal totalPaymentAmount,
			@RequestParam("downPaymentAmount") BigDecimal downPaymentAmount,
			@RequestParam("tenureYears") int tenureYears, @RequestParam("loanType") String loanType,
			@AuthenticationPrincipal UserDetails userDetails, Model model) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		if (user == null) {
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return LOAN_APPLICATION_ACTION;
		}
		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		boolean cardBlocked = platformService.isCardBlocked(accountNumber);
		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Loan application cannot be processed.");
			return LOAN_APPLICATION_ACTION;
		}

		try {
			Loan loan = platformService.createLoan(user.getUserId(), bankAccountId, totalPaymentAmount,
					downPaymentAmount, tenureYears, loanType);
			model.addAttribute("loan", loan);
			model.addAttribute(SUCCESS, "Loan application submitted successfully!");
		} catch (IllegalArgumentException e) {
			model.addAttribute(ERROR, e.getMessage());
		}

		return "loanApplicationResult";
	}

	@GetMapping("/loanHistory")
	public String showLoanHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		if (user == null) {
			model.addAttribute(ERROR, USER_NOT_FOUND_MESSAGE);
			return LOAN_HISTORY;
		}

		List<Loan> loanHistory = platformService.getLoanHistory(user.getUserId());
		model.addAttribute(LOAN_HISTORY, loanHistory);

		return LOAN_HISTORY;
	}

	@GetMapping("/payLoan")
	public String showPayLoanForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		Loan loan = platformService.getLoanByUserId(user.getUserId());
		BankAccount bankAccount = platformService.getBankAccountByUserId(user.getUserId());

		if (loan == null || bankAccount == null) {
			model.addAttribute(ERROR, "Loan or bank account information is missing.");
			return PAY_LOAN_ACTION;
		}

		BigDecimal remainingBalance = bankAccount.getBalance().subtract(loan.getEmiAmount());

		model.addAttribute("loan", loan);
		model.addAttribute("bankAccount", bankAccount);
		model.addAttribute("remainingBalance", remainingBalance);
		return PAY_LOAN_ACTION;
	}

	@PostMapping("/payLoan")
	public String payLoan(@RequestParam("loanId") int loanId, @AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		User user = platformService.getUserByEmail(userDetails.getUsername());
		Loan loan = platformService.getLoanByUserId(user.getUserId());
		BankAccount bankAccount = platformService.getBankAccountByUserId(user.getUserId());
		String accountNumber = platformService.getAccountNumberByUserId(user.getUserId());

		if (loan == null || bankAccount == null) {
			model.addAttribute(ERROR, "Loan or bank account information is missing.");
			return PAY_LOAN_ACTION;
		}

		boolean cardBlocked = platformService.isCardBlocked(accountNumber);
		if (cardBlocked) {
			model.addAttribute(ERROR, "Your card is blocked. Loan payment cannot be processed.");
			return PAY_LOAN_ACTION;
		}
		if (loan.isFullyPaid()) {
			model.addAttribute(ERROR, "You have already paid off the loan.");
		} else {
			try {
				platformService.payLoan(loan, bankAccount);
				model.addAttribute(SUCCESS, "EMI paid successfully!");
			} catch (IllegalArgumentException e) {
				model.addAttribute(ERROR, e.getMessage());
			}
		}

		BigDecimal remainingBalance = bankAccount.getBalance().subtract(loan.getEmiAmount());
		model.addAttribute("loan", loan);
		model.addAttribute("bankAccount", bankAccount);
		model.addAttribute("remainingBalance", remainingBalance);
		return PAY_LOAN_ACTION;
	}

}
