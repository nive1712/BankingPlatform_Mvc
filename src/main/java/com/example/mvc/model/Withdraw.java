package com.example.mvc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "withdraw")
public class Withdraw {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int withdrawId;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private BankAccount bankAccount;

	private BigDecimal amount;
	private Date withdrawDate;

	public int getWithdrawId() {
		return withdrawId;
	}

	public void setWithdrawId(int withdrawId) {
		this.withdrawId = withdrawId;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(Date withdrawDate) {
		this.withdrawDate = withdrawDate;
	}
}
