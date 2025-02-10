package com.example.mvc.imple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.mvc.model.BankAccount;
import com.example.mvc.model.Budget;
import com.example.mvc.model.CardBlockStatus;
import com.example.mvc.model.TransactionHistory;
import com.example.mvc.model.User;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class BankAccountDaoImpl {

    private static final Logger logger = LogManager.getLogger(BankAccountDaoImpl.class);
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String PENDING_STATUS = "Pending";
    private static final String USER_ID = "userId";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    private static final String ERROR_MESSAGE = "An error occurred while updating the bank account.";
    

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public String getTransactionHistory(int userId) {
        String transactionHistory;
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
        Root<TransactionHistory> root = criteriaQuery.from(TransactionHistory.class);
        criteriaQuery.select(root.get("transactionHistory"));

        criteriaQuery.where(criteriaBuilder.equal(root.get(USER_ID), userId));

        try {
            transactionHistory = session.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            transactionHistory = "No transactions have been recorded for the user.";
        }

        return transactionHistory;
    }

    public List<TransactionHistory> getTransactionsByBankAccount(String accountNumber, int pageNumber, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        Query<TransactionHistory> query = session.createQuery(
                "FROM TransactionHistory WHERE bankAccount.accountNumber = :accountNumber ORDER BY date DESC", TransactionHistory.class);
        query.setParameter(ACCOUNT_NUMBER, accountNumber);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }
    public List<CardBlockStatus> findAllCardBlockStatuses() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from CardBlockStatus", CardBlockStatus.class).list();
        }
    }
    
    
 
    public Integer getPinByUserId(int userId) {
        Session session = sessionFactory.openSession();
        Query<Integer> query = session.createQuery("SELECT pin FROM BankAccount WHERE userId = :userId", Integer.class);
        query.setParameter(USER_ID, userId);
        Integer pin = query.uniqueResult();
        session.close();
        return pin;
    }

    public int getUserIdByAccountNumber(String accountNumber) {
        Session session = sessionFactory.openSession();
        Query<Integer> query = session.createQuery("SELECT userId FROM BankAccount WHERE accountNumber = :accountNumber", Integer.class);
        query.setParameter(ACCOUNT_NUMBER, accountNumber);
        Integer userId = query.uniqueResult();
        session.close();
        return userId != null ? userId : -1;
    }

    public Integer getAccountIdByAccountNumber(String accountNumber) {
        Session session = sessionFactory.openSession();
        Query<Integer> query = session.createQuery("SELECT id FROM BankAccount WHERE accountNumber = :accountNumber", Integer.class);
        query.setParameter(ACCOUNT_NUMBER, accountNumber);
        Integer accountId = query.uniqueResult();
        session.close();
        return accountId;
    }

    public BankAccount getBankAccountByAccountNumber(String accountNumber) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM BankAccount WHERE accountNumber = :accountNumber", BankAccount.class)
                    .setParameter(ACCOUNT_NUMBER, accountNumber)
                    .uniqueResult();
        } catch (Exception e) {
           
            return null;
        }
    }

    public String generateAccountNumber() {
        return "ACC" + secureRandom.nextInt(1000000000);
    }

    public int generateNextTransId() {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COALESCE(MAX(b.transid), 0) + 1 FROM BankAccount b";
            TypedQuery<Integer> query = session.createQuery(hql, Integer.class);
            Integer maxTransId = query.getSingleResult();
            return maxTransId != null ? maxTransId : 1;
        } catch (Exception e) {
            logger.error("An error occurred while generating the next transaction ID.", e);
            return 1;
        }
    }


    public BankAccount getBankAccountByUserId(int userId) {
        Session session = sessionFactory.openSession();
        Query<BankAccount> query = session.createQuery("FROM BankAccount WHERE user.id = :userId", BankAccount.class);
        query.setParameter(USER_ID, userId);
        BankAccount bankAccount = query.uniqueResult();
        session.close();
        return bankAccount;
    }
    
    public Optional<BankAccount> findById(int id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        BankAccount bankAccount = null;
        try {
            transaction = session.beginTransaction();
            bankAccount = session.get(BankAccount.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Optional.ofNullable(bankAccount);
    }


    public BankAccount getBankAccountByAccountId(int accountId) {
        return sessionFactory.getCurrentSession().createQuery("FROM BankAccount WHERE accountId = :accountId", BankAccount.class)
                .setParameter("accountId", accountId)
                .uniqueResult();
    }

    
    public boolean createBankAccount(BankAccount bankAccount) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(bankAccount);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean updateBankAccount(BankAccount bankAccount) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.merge(bankAccount);
            return true;
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return false;
        }
    }
    
    public CardBlockStatus getCardBlockStatusById(int id) {
        return sessionFactory.getCurrentSession().get(CardBlockStatus.class, id);
    }
    
    public void updateCardBlockStatus(CardBlockStatus cardBlockStatus) {
        sessionFactory.getCurrentSession().merge(cardBlockStatus);
    }

    public String getAccountNumberByUserId(int userId) {
        Session session = sessionFactory.openSession();
        Query<String> query = session.createQuery("SELECT accountNumber FROM BankAccount WHERE userId = :userId", String.class);
        query.setParameter(USER_ID, userId);
        String accountNumber = query.uniqueResult();
        session.close();
        return accountNumber;
    }

    public User showCompleteDetails(int accountId, String accountNumber) {
        Session session = sessionFactory.getCurrentSession();
        try {
            String hql = "FROM User u " +
                         "LEFT JOIN FETCH u.bankAccount ba " +
                         "LEFT JOIN FETCH ba.cardBlockStatus " +
                         "LEFT JOIN FETCH u.loan " +
                         "WHERE ba.accountId = :accountId AND ba.accountNumber = :accountNumber";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("accountId", accountId);
            query.setParameter(ACCOUNT_NUMBER, accountNumber);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return null;
        }
    }
    public boolean blockCard(int userId, String accountNumber, int pin, String reason) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<BankAccount> query = session.createQuery(
                "FROM BankAccount WHERE user.userId = :userId AND accountNumber = :accountNumber AND pin = :pin",
                BankAccount.class);
            query.setParameter( USER_ID , userId);
            query.setParameter(ACCOUNT_NUMBER, accountNumber);
            query.setParameter("pin", pin);

            BankAccount bankAccount = query.uniqueResult();

            if (bankAccount != null) {
                CardBlockStatus cardBlockStatus = new CardBlockStatus();
                cardBlockStatus.setBankAccount(bankAccount);
                cardBlockStatus.setBlockDate(LocalDate.now());
                cardBlockStatus.setBlockReason(reason);
                cardBlockStatus.setCardBlocked(true);
                cardBlockStatus.setStatus( PENDING_STATUS );
                cardBlockStatus.setRequest( PENDING_STATUS );
                cardBlockStatus.setAdminApproval("Not Approved");

                session.persist(cardBlockStatus);

                transaction.commit();
                logger.info("Card block status saved successfully.");
                return true;
            } else {
                if (transaction != null) transaction.rollback();
                logger.warn("No bank account found for userId: {}, accountNumber: {}", userId, accountNumber);
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error blocking card: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean unblockCard(int userId, String accountNumber, int pin) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<BankAccount> query = session.createQuery(
                "FROM BankAccount WHERE user.userId = :userId AND accountNumber = :accountNumber AND pin = :pin",
                BankAccount.class);
            query.setParameter( USER_ID , userId);
            query.setParameter(ACCOUNT_NUMBER, accountNumber);
            query.setParameter("pin", pin);

            BankAccount bankAccount = query.uniqueResult();

            if (bankAccount != null) {
                Query<CardBlockStatus> cardBlockQuery = session.createQuery(
                    "FROM CardBlockStatus WHERE bankAccount = :bankAccount AND isCardBlocked = true",
                    CardBlockStatus.class);
                cardBlockQuery.setParameter("bankAccount", bankAccount);

                CardBlockStatus cardBlockStatus = cardBlockQuery.uniqueResult();

                if (cardBlockStatus != null) {
                    cardBlockStatus.setCardBlocked(false);
                    cardBlockStatus.setUnblockDate(LocalDate.now());
                    cardBlockStatus.setStatus("Unblocked");
                    cardBlockStatus.setRequest( PENDING_STATUS );
                    cardBlockStatus.setAdminApproval("Not Approved");

                    session.merge(cardBlockStatus);

                    transaction.commit();
                    return true;
                } else {
                    if (transaction != null) transaction.rollback();
                    return false;
                }
            } else {
                if (transaction != null) transaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error unblocking card: {}", e.getMessage(), e);
            return false;
        }
    }

    public boolean saveBudget(Budget budget) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(budget);
            transaction.commit();
            logger.info("Budget saved successfully.");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error saving budget: {}", e.getMessage(), e);
            return false;
        }
    }

    public Budget getBudgetById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Budget.class, id);
        } catch (Exception e) {
            logger.error("Error retrieving budget by id: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean updateBudget(Budget budget) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(budget);
            transaction.commit();
            logger.info("Budget updated successfully.");
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Error updating budget: {}", e.getMessage(), e);
            return false;
        }
    }

    public List<Budget> getAllBudgets() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Budget", Budget.class).list();
        } catch (Exception e) {
            logger.error("Error retrieving all budgets: {}", e.getMessage(), e);
            return Collections.emptyList(); // Return an empty list instead of null
        }
    }

    public boolean depositAmount(BigDecimal amount) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction transaction = session.beginTransaction();

        try {
            TransactionHistory depositTransaction = new TransactionHistory();
            depositTransaction.setAmount(amount);
            depositTransaction.setDate(LocalDate.now());
            depositTransaction.setType("Deposit");
            depositTransaction.setDescription("Deposit transaction");
            session.persist(depositTransaction);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error(ERROR_MESSAGE, e);
            return false; 
        } finally {
            session.close();
        }
    }
    
    public void saveOrUpdate(BankAccount bankAccount) {
        sessionFactory.getCurrentSession().merge(bankAccount);
    }
     public boolean isCardBlocked(String accountNumber) {
        Session session = sessionFactory.openSession();
        Query<CardBlockStatus> query = session.createQuery(
            "SELECT cbs FROM CardBlockStatus cbs JOIN cbs.bankAccount ba WHERE ba.accountNumber = :accountNumber AND cbs.request = 'approved'",
            CardBlockStatus.class);
        query.setParameter(ACCOUNT_NUMBER, accountNumber);
        boolean isBlocked = query.uniqueResult() != null;
        session.close();
        return isBlocked;
    }
     
   
     public BankAccount findBankAccountByUserId(int userId) {
         String hql = "FROM BankAccount WHERE user.userId = :userId";
         return sessionFactory.getCurrentSession().createQuery(hql, BankAccount.class)
                 .setParameter( USER_ID , userId)
                 .uniqueResult();
     }
       
}
