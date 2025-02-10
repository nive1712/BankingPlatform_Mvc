package com.example.mvc.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.mvc.model.BankAccount;
import com.example.mvc.model.Budget;
import com.example.mvc.model.CardBlockStatus;

import com.example.mvc.model.Loan;
import com.example.mvc.model.NetBanking;
import com.example.mvc.model.TransactionHistory;
import com.example.mvc.model.User;

public interface PlatformService {
	int registerUser(User user, String password);

	User loginUser(String email, String password);

	void createBankAccount(BankAccount bankAccount);

	boolean deposit(int userId, double amount);

	NetBanking getNetBankingByUserId(int userId);

	void handleTransfer(String senderAccountNumber, String recipientAccountNumber, BigDecimal transferAmount);

	void handleDeposit(int userId, BigDecimal amount);

	User getUserById(int userId);

	boolean withdraw(int userId, double amount);

	boolean depositAmount(int userId, BigDecimal amount);

	boolean netBankingDeposit(int userId, double amount);

	List<TransactionHistory> getTransactionHistory(int userId);

	User getUserByEmail(String email);

	void save(User user);

	boolean authenticate(String accountNumber, int pin);

	int getUserIdByAccountNumber(String accountNumber);

	boolean transfer(int userId, String senderAccountNumber, String recipientAccountNumber, BigDecimal transferAmount);

	String getAccountNumberByUserId(int userId);

	boolean isCardBlocked(String accountNumber);

	Integer getPinByUserId(int userId);

	boolean blockCard(int userId, String accountNumber, int pin, String reason);

	boolean unblockCard(int userId, String accountNumber, int pin);

	String showCompleteDetails(int accountId);

	int getAccountIdByAccountNumber(String accountNumber);

	List<TransactionHistory> transactionDetails(int accountId);

	List<TransactionHistory> getNetbankingTransactions(int accountId);

	List<TransactionHistory> getTransactionsByBankAccount(String accountNumber, int pageNumber, int pageSize);

	CardBlockStatus getCardBlockStatusByAccountNumber(String accountNumber);

	void approveCardBlockStatus(int id);

	List<CardBlockStatus> getAllCardBlockStatuses();

	Budget calculateBudget(Budget budget);

	Loan createLoan(int userId, int bankAccountId, BigDecimal totalPaymentAmount, BigDecimal downPaymentAmount,
			int tenureYears, String loanType);

	List<Loan> getLoanHistory(int userId);

	BankAccount getBankAccountByUserId(int userId);

	void payLoan(Loan loan, BankAccount bankAccount);

	Loan getLoanByUserId(int userId);

	void rejectCardBlockStatus(int id);

}
