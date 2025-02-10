package com.example.mvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "bank_account")
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "accountId")
	private int accountId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", referencedColumnName = "userId")
	private User user;

	private String accountNumber;
	private int mfaPin;
	private BigDecimal initialBalance;
	private int pin;
	private BigDecimal balance;

	@OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Deposit> deposits;

	@OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Withdraw> withdraws;

	@OneToMany(mappedBy = "sourceAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transfer> sentTransfers;

	@OneToMany(mappedBy = "targetAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transfer> receivedTransfers;

	@OneToOne(mappedBy = "bankAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CardBlockStatus cardBlockStatus;

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public int getMfaPin() {
		return mfaPin;
	}

	public void setMfaPin(int mfaPin) {
		this.mfaPin = mfaPin;
	}

	public BigDecimal getInitialBalance() {
		return initialBalance;
	}

	public void setInitialBalance(BigDecimal initialBalance) {
		this.initialBalance = initialBalance;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	public List<Withdraw> getWithdraws() {
		return withdraws;
	}

	public void setWithdraws(List<Withdraw> withdraws) {
		this.withdraws = withdraws;
	}

	public List<Transfer> getSentTransfers() {
		return sentTransfers;
	}

	public void setSentTransfers(List<Transfer> sentTransfers) {
		this.sentTransfers = sentTransfers;
	}

	public List<Transfer> getReceivedTransfers() {
		return receivedTransfers;
	}

	public void setReceivedTransfers(List<Transfer> receivedTransfers) {
		this.receivedTransfers = receivedTransfers;
	}

	public CardBlockStatus getCardBlockStatus() {
		return cardBlockStatus;
	}

	public void setCardBlockStatus(CardBlockStatus cardBlockStatus) {
		this.cardBlockStatus = cardBlockStatus;
	}
}
