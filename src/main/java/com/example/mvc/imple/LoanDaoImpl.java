package com.example.mvc.imple;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.mvc.model.Loan;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public class LoanDaoImpl {

	private static final String USER_ID = "userId";
   
	 private final SessionFactory sessionFactory;

	    @Autowired
	    public LoanDaoImpl(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
    
    
    public List<Loan> findLoansByUserId(int userId) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Loan WHERE user.userId = :userId", Loan.class)
                .setParameter( USER_ID , userId)
                .getResultList();
    }

    

    public Loan getLoanByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Loan> query = session.createQuery("FROM Loan WHERE user.userId = :userId", Loan.class);
        query.setParameter( USER_ID , userId);
        return query.uniqueResult();
    }
    
   
    public Loan save(Loan loan) {
        sessionFactory.getCurrentSession().persist(loan);
		return loan;
    }
    
    
    public void updateLoan(Loan loan) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(loan);
    }

    public Loan findLoanByUserId(int userId) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Loan WHERE user.id = :userId";
        Query<Loan> query = session.createQuery(hql, Loan.class);
        query.setParameter( USER_ID , userId);
        return query.uniqueResult();
    }
    
    
    public void saveLoan(Loan loan) {
        sessionFactory.getCurrentSession().merge(loan);
    }
}
