package com.example.mvc.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mvc.exceptions.AccountNumberNotFoundException;
import com.example.mvc.exceptions.BudgetProcessingException;
import com.example.mvc.imple.BankAccountDaoImpl;
import com.example.mvc.imple.LoanDaoImpl;
import com.example.mvc.imple.NetBankingDaoImpl;
import com.example.mvc.imple.UserDaoImpl;
import com.example.mvc.model.BankAccount;
import com.example.mvc.model.Budget;
import com.example.mvc.model.CardBlockStatus;
import com.example.mvc.model.Deposit;
import com.example.mvc.model.Loan;
import com.example.mvc.model.NetBanking;
import com.example.mvc.model.TransactionHistory;
import com.example.mvc.model.Transfer;
import com.example.mvc.model.User;
import com.example.mvc.model.Withdraw;
import com.example.mvc.service.PlatformService;

@Service
public class PlatformServiceImpl implements PlatformService {

	private static final String TRANSACTION_HISTORY_RETRIEVAL_ERROR = "Failed to retrieve transaction history.";

	private static final String ACCOUNT_ID = "accountId";
	Scanner scanner = new Scanner(System.in);

	private static final Logger logger = LogManager.getLogger(PlatformServiceImpl.class);

	private SessionFactory sessionFactory;
	private UserDaoImpl userDao;
	private NetBankingDaoImpl netBankingDao;
	private BankAccountDaoImpl bankAccountDao;
	
	@Autowired
	private LoanDaoImpl loanDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Autowired
	private PlatformService platformService;

	@Autowired
	public void setUserDao(UserDaoImpl userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setNetBankingDao(NetBankingDaoImpl netBankingDao) {
		this.netBankingDao = netBankingDao;
	}

	@Autowired
	public void setBankAccountDao(BankAccountDaoImpl bankAccountDao) {
		this.bankAccountDao = bankAccountDao;
	}

	@Override
	@Transactional
	public int registerUser(User user, String password) {
		int userId = userDao.saveUser(user);

		if (userId != -1) {

			NetBanking netBanking = new NetBanking();
			netBanking.setUser(user);
			netBanking.setPassword(password);
			netBankingDao.saveNetBanking(netBanking);
		}
		return userId;
	}

	@Override
	@Transactional
	public User loginUser(String email, String password) {
		User user = userDao.getUserByEmail(email);
		if (user != null) {
			NetBanking netBanking = netBankingDao.getNetBankingByUserId(user.getUserId());
			if (netBanking != null && netBanking.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public void handleDeposit(int userId, BigDecimal amount) {

		BankAccount bankAccount = bankAccountDao.getBankAccountByUserId(userId);
		if (bankAccount != null) {
			BigDecimal newBalance = bankAccount.getBalance().add(amount);
			bankAccount.setBalance(newBalance);

			try (Session session = sessionFactory.openSession()) {
				org.hibernate.Transaction transaction = session.beginTransaction();

				session.merge(bankAccount);

				Deposit deposit = new Deposit();
				deposit.setBankAccount(bankAccount);
				deposit.setAmount(amount);
				deposit.setDepositDate(LocalDate.now());

				session.persist(deposit);

				TransactionHistory depositTransaction = new TransactionHistory();
				depositTransaction.setBankAccount(bankAccount);
				depositTransaction.setAmount(amount);
				depositTransaction.setDate(LocalDate.now());
				depositTransaction.setType("Deposit Action");
				depositTransaction.setDescription("Deposit transaction");

				session.persist(depositTransaction);

				transaction.commit();
				logger.info("Deposit successful. New balance: {}", newBalance);
			} catch (Exception e) {
				logger.error("Failed to deposit amount due to an error.", e);
			}
		} else {
			logger.info("Bank account not found.");
		}
	}

	@Override
	@Transactional
	public void createBankAccount(BankAccount bankAccount) {
		bankAccount.setBalance(BigDecimal.valueOf(500));
		bankAccount.setInitialBalance(BigDecimal.valueOf(500));

		bankAccountDao.createBankAccount(bankAccount);

	}

	@Override
	@Transactional
	public void handleTransfer(String senderAccountNumber, String recipientAccountNumber, BigDecimal transferAmount) {
	    Session session = null;
	    Transaction transaction = null;

	    try {
	        session = sessionFactory.openSession();
	        transaction = session.beginTransaction();

	        BankAccount senderAccount = bankAccountDao.getBankAccountByAccountNumber(senderAccountNumber);
	        BankAccount recipientAccount = bankAccountDao.getBankAccountByAccountNumber(recipientAccountNumber);

	        if (senderAccount != null && recipientAccount != null
	                && senderAccount.getBalance().compareTo(transferAmount) >= 0) {
	            BigDecimal newSenderBalance = senderAccount.getBalance().subtract(transferAmount);
	            senderAccount.setBalance(newSenderBalance);

	            BigDecimal newRecipientBalance = recipientAccount.getBalance().add(transferAmount);
	            recipientAccount.setBalance(newRecipientBalance);

	            session.merge(senderAccount); // Use merge instead of update
	            session.merge(recipientAccount); // Use merge instead of update

	            Transfer transfer = new Transfer();
	            transfer.setSourceAccount(senderAccount);
	            transfer.setTargetAccount(recipientAccount);
	            transfer.setAmount(transferAmount);
	            transfer.setTransferDate(LocalDateTime.now());
	            session.persist(transfer); // Use persist instead of save

	            TransactionHistory senderTransaction = new TransactionHistory();
	            senderTransaction.setBankAccount(senderAccount);
	            senderTransaction.setAmount(transferAmount.negate());
	            senderTransaction.setDate(LocalDate.now());
	            senderTransaction.setType("Transfer");
	            senderTransaction.setDescription("Transfer to account " + recipientAccountNumber);

	            TransactionHistory recipientTransaction = new TransactionHistory();
	            recipientTransaction.setBankAccount(recipientAccount);
	            recipientTransaction.setAmount(transferAmount);
	            recipientTransaction.setDate(LocalDate.now());
	            recipientTransaction.setType("Transfer");
	            recipientTransaction.setDescription("Transfer from account " + senderAccountNumber);

	            session.persist(senderTransaction); // Use persist instead of save
	            session.persist(recipientTransaction); // Use persist instead of save

	            transaction.commit();
	            logger.info("Transfer successful. Amount: {}", transferAmount);
	        } else {
	            if (senderAccount == null) {
	                logger.info("Sender's bank account not found.");
	            } else if (recipientAccount == null) {
	                logger.info("Recipient's bank account not found.");
	            } else {
	                logger.info("Insufficient balance.");
	            }
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        logger.error("Failed to transfer amount due to an error.", e);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	}


	@Override
	@Transactional
	public void save(User user) {

		UserDaoImpl userDaoImpl = new UserDaoImpl();
		userDaoImpl.saveUser(user);
	}

	@Override
	@Transactional
	public CardBlockStatus getCardBlockStatusByAccountNumber(String accountNumber) {
		try (Session session = sessionFactory.openSession()) {
			Query<CardBlockStatus> query = session.createQuery(
					"FROM CardBlockStatus WHERE bankAccount.accountNumber = :accountNumber", CardBlockStatus.class);
			query.setParameter("accountNumber", accountNumber);
			return query.uniqueResult();
		} catch (Exception e) {
			logger.error("Error retrieving CardBlockStatus: {}", e.getMessage(), e);
			return null;
		}
	}

	@Override
	@Transactional
	public boolean deposit(int userId, double amount) {
		BankAccount bankAccount = bankAccountDao.getBankAccountByUserId(userId);
		if (bankAccount == null) {
			return false;
		}
		BigDecimal newBalance = bankAccount.getBalance().add(BigDecimal.valueOf(amount));
		bankAccount.setBalance(newBalance);

		Deposit deposit = new Deposit();
		deposit.setBankAccount(bankAccount);
		deposit.setAmount(BigDecimal.valueOf(amount));
		deposit.setDepositDate(LocalDate.now());

		TransactionHistory depositTransaction = new TransactionHistory();
		depositTransaction.setBankAccount(bankAccount);
		depositTransaction.setAmount(BigDecimal.valueOf(amount));
		depositTransaction.setDate(LocalDate.now());
		depositTransaction.setDescription("Deposit transaction");
		depositTransaction.setType("Deposit");

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			session.merge(bankAccount);

			session.persist(deposit);

			session.persist(depositTransaction);

			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Failed to deposit amount due to an error.", e);
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	@Transactional
	public List<TransactionHistory> transactionDetails(int accountId) {
		try (Session session = sessionFactory.openSession()) {
			Query<TransactionHistory> query = session.createQuery(
					"FROM TransactionHistory WHERE bankAccount.accountId = :accountId "
							+ "AND (type = 'ATM Deposit' OR type = 'ATM Withdrawal' OR type = 'ATM Transfer')",
					TransactionHistory.class);
			query.setParameter(ACCOUNT_ID, accountId);
			List<TransactionHistory> transactions = query.getResultList();

			for (TransactionHistory transaction : transactions) {
				logger.info("-----------------------------------------");
				logger.info("Transaction ID:{} ", transaction.getTransactionId());
				logger.info("Amount: {}", transaction.getAmount());
				logger.info("Date: {}", transaction.getDate());
				logger.info("Description: {}", transaction.getDescription());
				logger.info("Type: {}", transaction.getType());
				logger.info("-----------------------------------------");
			}

			return transactions;
		} catch (Exception e) {
			logger.error(TRANSACTION_HISTORY_RETRIEVAL_ERROR, e);
			return Collections.emptyList();
		}
	}

	@Override
	@Transactional
	public Loan createLoan(int userId, int bankAccountId, BigDecimal totalPaymentAmount, BigDecimal downPaymentAmount,
			int tenureYears, String loanType) {

		Optional<BankAccount> optionalBankAccount = bankAccountDao.findById(bankAccountId);

		if (!optionalBankAccount.isPresent()) {
			throw new IllegalArgumentException("No bank account found for the provided account ID.");
		}

		BankAccount bankAccount = optionalBankAccount.get();

		BigDecimal loanAmount = totalPaymentAmount.subtract(downPaymentAmount);
		double interestRate = calculateInterestRate(tenureYears);
		double emi = calculateEMI(loanAmount, interestRate, tenureYears);

		Loan loan = new Loan();
		loan.setUser(bankAccount.getUser());
		loan.setBankAccountId(bankAccountId);
		loan.setTotalPayment(totalPaymentAmount);
		loan.setDownPayment(downPaymentAmount);
		loan.setInterestRate(BigDecimal.valueOf(interestRate));
		loan.setTenureYears(tenureYears);
		loan.setEmiAmount(BigDecimal.valueOf(emi));
		loan.setSanctionDate(new Date());
		loan.setFullyPaid(false);
		loan.setPaidInMonths(0);
		loan.setPayableMonth(tenureYears * 12);
		loan.setLoanType(loanType);
		return loanDao.save(loan);
	}

	private double calculateInterestRate(int tenureYears) {
		if (tenureYears >= 5 && tenureYears <= 15) {
			return 8.0 + (tenureYears - 5) * 1.0;
		} else if (tenureYears >= 2 && tenureYears <= 8) {
			return 7.0 + (tenureYears - 2) * 0.5;
		} else if (tenureYears >= 1 && tenureYears <= 5) {
			return 10.0 + (tenureYears - 1) * 1.0;
		} else {
			throw new IllegalArgumentException("Invalid tenure. Tenure must be within the supported ranges.");
		}
	}

	private double calculateEMI(BigDecimal loanAmount, double interestRate, int tenureYears) {
		double monthlyInterestRate = interestRate / 12 / 100;
		int numberOfPayments = tenureYears * 12;
		return (loanAmount.doubleValue() * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, numberOfPayments))
				/ (Math.pow(1 + monthlyInterestRate, numberOfPayments) - 1);
	}

	@Override
	@Transactional
	public void payLoan(Loan loan, BankAccount bankAccount) throws IllegalArgumentException {
		BigDecimal emiAmount = loan.getEmiAmount();
		BigDecimal currentBalance = bankAccount.getBalance();

		if (currentBalance.compareTo(emiAmount) < 0) {
			throw new IllegalArgumentException("Insufficient balance to pay EMI.");
		}

		bankAccount.setBalance(currentBalance.subtract(emiAmount));
		loan.setPaidInMonths(loan.getPaidInMonths() + 1);

		if (loan.getPaidInMonths() >= loan.getPayableMonth()) {
			loan.setFullyPaid(true);
		}

		bankAccountDao.updateBankAccount(bankAccount);
		loanDao.updateLoan(loan);
	}

	@Override
	@Transactional
	public Loan getLoanByUserId(int userId) {
		Loan loan = loanDao.findLoanByUserId(userId);
		if (loan == null) {
			throw new IllegalArgumentException("Loan not found for User ID: " + userId);
		}
		return loan;
	}

	@Override
	@Transactional
	public List<Loan> getLoanHistory(int userId) {
		return loanDao.findLoansByUserId(userId);
	}

	@Override
	@Transactional
	public List<TransactionHistory> getNetbankingTransactions(int accountId) {
		try (Session session = sessionFactory.openSession()) {
			Query<TransactionHistory> query = session.createQuery(
					"FROM TransactionHistory WHERE bankAccount.accountId = :accountId "
							+ "AND (type = 'Netbanking Deposit' OR type = 'Netbanking Transfer')",
					TransactionHistory.class);
			query.setParameter(ACCOUNT_ID, accountId);
			List<TransactionHistory> transactions = query.getResultList();

			for (TransactionHistory transaction : transactions) {
				logger.info("Transaction ID service: {}", transaction.getTransactionId());
				logger.info("Amount: {}", transaction.getAmount());
				logger.info("Date: {}", transaction.getDate());
				logger.info("Description: {}", transaction.getDescription());
				logger.info("Type: {}", transaction.getType());
				logger.info("-------------------------------");
			}

			return transactions;
		} catch (Exception e) {
			logger.error(TRANSACTION_HISTORY_RETRIEVAL_ERROR, e);
			return Collections.emptyList();
		}
	}

	@Override
	@Transactional
	public boolean withdraw(int userId, double amount) {
		BankAccount bankAccount = bankAccountDao.getBankAccountByUserId(userId);
		if (bankAccount == null || bankAccount.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
			return false;
		}

		BigDecimal newBalance = bankAccount.getBalance().subtract(BigDecimal.valueOf(amount));
		bankAccount.setBalance(newBalance);

		Withdraw withdraw = new Withdraw();
		withdraw.setBankAccount(bankAccount);
		withdraw.setAmount(BigDecimal.valueOf(amount));
		withdraw.setWithdrawDate(new Date());

		TransactionHistory withdrawalTransaction = new TransactionHistory();
		withdrawalTransaction.setBankAccount(bankAccount);
		withdrawalTransaction.setAmount(BigDecimal.valueOf(amount));
		withdrawalTransaction.setDate(LocalDate.now());
		withdrawalTransaction.setDescription("Withdrawal transaction");
		withdrawalTransaction.setType("ATM Withdrawal");

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();

			session.merge(bankAccount);

			session.persist(withdraw);

			session.persist(withdrawalTransaction);

			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error("Failed to withdraw amount due to an error.", e);
			return false;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	@Transactional
	public boolean netBankingDeposit(int userId, double amount) {
		return deposit(userId, amount);
	}

	@Override
	@Transactional
	public void approveCardBlockStatus(int id) {
		CardBlockStatus cardBlockStatus = bankAccountDao.getCardBlockStatusById(id);
		if (cardBlockStatus != null) {
			cardBlockStatus.setAdminApproval("Approved");
			cardBlockStatus.setRequest("Approved");
			bankAccountDao.updateCardBlockStatus(cardBlockStatus);
		}
	}

	@Override
	@Transactional
	public void rejectCardBlockStatus(int id) {
		CardBlockStatus cardBlockStatus = bankAccountDao.getCardBlockStatusById(id);
		if (cardBlockStatus != null) {
			cardBlockStatus.setAdminApproval("Rejected");
			cardBlockStatus.setRequest("Rejected");
			bankAccountDao.updateCardBlockStatus(cardBlockStatus);
		}
	}

	@Override
	public List<CardBlockStatus> getAllCardBlockStatuses() {
		return bankAccountDao.findAllCardBlockStatuses();
	}

	@Override
	@Transactional
	public List<TransactionHistory> getTransactionHistory(int userId) {
		BankAccount bankAccount = bankAccountDao.getBankAccountByUserId(userId);
		if (bankAccount != null) {
			try (Session session = sessionFactory.openSession()) {
				return session
						.createQuery("FROM TransactionHistory th WHERE th.bankAccount.accountId = :accountId",
								TransactionHistory.class)
						.setParameter(ACCOUNT_ID, bankAccount.getAccountId()).getResultList();
			} catch (Exception e) {
				logger.error(TRANSACTION_HISTORY_RETRIEVAL_ERROR, e);
				return Collections.emptyList();
			}
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	@Transactional
	public boolean authenticate(String accountNumber, int pin) {
		int userId = getUserIdByAccountNumber(accountNumber);
		if (userId != -1) {
			BankAccount bankAccount = bankAccountDao.getBankAccountByUserId(userId);
			if (bankAccount != null && bankAccount.getPin() == pin) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Transactional
	public Integer getPinByUserId(int userId) {
		return bankAccountDao.getPinByUserId(userId);
	}

	@Override
	@Transactional
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	@Transactional
	public BankAccount getBankAccountByUserId(int userId) {
		return bankAccountDao.findBankAccountByUserId(userId);
	}

	@Override
	@Transactional
	public int getUserIdByAccountNumber(String accountNumber) {
		BankAccount bankAccount = bankAccountDao.getBankAccountByAccountNumber(accountNumber);
		if (bankAccount != null) {
			return bankAccount.getUser().getUserId();
		}
		return -1;
	}

	@Override
	@Transactional
	public String getAccountNumberByUserId(int userId) {
		BankAccount bank = bankAccountDao.getBankAccountByUserId(userId);
		if (bank != null) {
			return bank.getAccountNumber();
		}
		return null;
	}

	@Override
	@Transactional
	public boolean depositAmount(int userId, BigDecimal amount) {
		return bankAccountDao.depositAmount(amount);
	}

	@Override
	@Transactional
	public boolean transfer(int userId, String sourceAccountNumber, String targetAccountNumber, BigDecimal amount) {
	    try {
	        Session session = sessionFactory.getCurrentSession();

	        BankAccount sourceAccount = bankAccountDao.getBankAccountByAccountNumber(sourceAccountNumber);
	        BankAccount targetAccount = bankAccountDao.getBankAccountByAccountNumber(targetAccountNumber);

	        if (sourceAccount == null || targetAccount == null) {
	            return false;
	        }

	        if (sourceAccount.getBalance().compareTo(amount) < 0) {
	            return false;
	        }

	        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(amount);
	        BigDecimal newTargetBalance = targetAccount.getBalance().add(amount);

	        sourceAccount.setBalance(newSourceBalance);
	        targetAccount.setBalance(newTargetBalance);

	        session.merge(sourceAccount);
	        session.merge(targetAccount);

	        Transfer transfer = new Transfer();
	        transfer.setSourceAccount(sourceAccount);
	        transfer.setTargetAccount(targetAccount);
	        transfer.setAmount(amount);
	        transfer.setTransferDate(LocalDateTime.now());
	        session.persist(transfer);

	        TransactionHistory sourceTransaction = new TransactionHistory();
	        sourceTransaction.setBankAccount(sourceAccount);
	        sourceTransaction.setAmount(amount.negate());
	        sourceTransaction.setDate(LocalDate.now());
	        sourceTransaction.setType("ATM Transfer");
	        sourceTransaction.setDescription("Transfer to " + targetAccountNumber);

	        session.persist(sourceTransaction);

	        TransactionHistory targetTransaction = new TransactionHistory();
	        targetTransaction.setBankAccount(targetAccount);
	        targetTransaction.setAmount(amount);
	        targetTransaction.setDate(LocalDate.now());
	        targetTransaction.setType("ATM Transfer");
	        targetTransaction.setDescription("Transfer from " + sourceAccountNumber);

	        session.persist(targetTransaction);

	        return true;
	    } catch (Exception e) {
	        logger.error("An error occurred while updating the bank account.", e);
	        return false;
	    }
	}

	@Override
	@Transactional
	public boolean blockCard(int userId, String accountNumber, int pin, String reason) {
		return bankAccountDao.blockCard(userId, accountNumber, pin, reason);
	}

	@Override
	@Transactional
	public boolean unblockCard(int userId, String accountNumber, int pin) {
		return bankAccountDao.unblockCard(userId, accountNumber, pin);
	}

	@Override
	@Transactional
	public boolean isCardBlocked(String accountNumber) {
		return bankAccountDao.isCardBlocked(accountNumber);
	}

	@Override
	@Transactional
	public User getUserById(int userId) {
		return userDao.getUserById(userId);
	}

	@Override
	@Transactional
	public int getAccountIdByAccountNumber(String accountNumber) {
		try (Session session = sessionFactory.openSession()) {
			Query<Integer> query = session.createQuery(
					"SELECT b.accountId FROM BankAccount b WHERE b.accountNumber = :accountNumber", Integer.class);
			query.setParameter("accountNumber", accountNumber);

			Integer accountId = query.uniqueResult();
			if (accountId != null) {
				return accountId;
			} else {
				throw new AccountNumberNotFoundException("Account number not found: " + accountNumber);
			}
		} catch (HibernateException e) {
			throw new AccountNumberNotFoundException("Error fetching account ID for account number: " + accountNumber,
					e);
		}
	}

	@Override
	@Transactional
	public List<TransactionHistory> getTransactionsByBankAccount(String accountNumber, int pageNumber, int pageSize) {
		return bankAccountDao.getTransactionsByBankAccount(accountNumber, pageNumber, pageSize);
	}

	@Override
	@Transactional
	public String showCompleteDetails(int accountId) {
		try (Session session = sessionFactory.openSession()) {

			BankAccount bankAccount = session.get(BankAccount.class, accountId);

			if (bankAccount == null) {
				return "No bank account found for account ID: " + accountId;
			}

			User user = bankAccount.getUser();
			if (user == null) {
				return "No user found for bank account ID: " + accountId;
			}

			StringBuilder details = new StringBuilder();
			details.append("Complete Details for Account ID ").append(accountId).append(":\n");
			details.append("-----------------------------\n");

			details.append("User ID: ").append(user.getUserId()).append("\n");
			details.append("Name: ").append(user.getName()).append("\n");
			details.append("Email: ").append(user.getEmail()).append("\n");
			details.append("Phone Number: ").append(user.getPhoneNumber()).append("\n");

			CardBlockStatus cardBlockStatus = bankAccount.getCardBlockStatus();
			if (cardBlockStatus != null) {
				details.append("Card Block Status:\n");
				details.append("Blocked: ").append(cardBlockStatus.isCardBlocked()).append("\n");
			} else {
				details.append("No card block status found for the account.\n");
			}
			details.append("-----------------------------\n");

			List<TransactionHistory> transactions = session
					.createQuery("FROM TransactionHistory th WHERE th.bankAccount.accountId = :accountId",
							TransactionHistory.class)
					.setParameter(ACCOUNT_ID, accountId).getResultList();

			if (transactions.isEmpty()) {
				details.append("No transactions found for account ID: ").append(accountId).append("\n");
			} else {
				details.append("Transaction History:\n");
				for (TransactionHistory transaction : transactions) {
					details.append("-----------------\n");
					details.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
					details.append("Amount: ").append(transaction.getAmount()).append("\n");
					details.append("Date: ").append(transaction.getDate()).append("\n");
					details.append("Description: ").append(transaction.getDescription()).append("\n");
					details.append("Type: ").append(transaction.getType()).append("\n");
				}
				details.append("-----------------------------------------------------------\n");
			}

			return details.toString();
		} catch (Exception e) {
			logger.error("Failed to retrieve complete details.", e);
			return "Failed to retrieve complete details.";
		}
	}

	@Transactional
	public NetBanking getNetBankingByUserId(int userId) {
		return netBankingDao.getNetBankingByUserId(userId);
	}

	@Override
	@Transactional
	public Budget calculateBudget(Budget budget) {
		BigDecimal totalIncome = budget.getTotalIncome();
		BigDecimal totalExpenses = budget.getTotalExpenses();
		BigDecimal debtRepayment = budget.getDebtRepayment();

		BigDecimal budgetBalance = totalIncome.subtract(totalExpenses.add(debtRepayment));
		budget.setBudgetBalance(budgetBalance);

		if (bankAccountDao.saveBudget(budget)) {
			return budget;
		} else {
			throw new BudgetProcessingException("Failed to save the budget.");
		}

	}

}
