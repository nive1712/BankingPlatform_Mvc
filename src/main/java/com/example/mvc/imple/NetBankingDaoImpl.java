package com.example.mvc.imple;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.mvc.model.NetBanking;

@Repository
public class NetBankingDaoImpl {

	private static final Logger logger = LogManager.getLogger( NetBankingDaoImpl.class);
    private final SessionFactory sessionFactory;

    @Autowired
    public NetBankingDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void saveNetBanking(NetBanking netBanking) {
        if (netBanking == null) {
            throw new IllegalArgumentException("NetBanking cannot be null");
        }
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(netBanking);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Failed to save NetBanking due to an error.", e);
            throw e;
        }
    }

    @Transactional
    public NetBanking getNetBankingByAccountId(int accountId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM NetBanking WHERE user.userId = (SELECT user.userId FROM BankAccount WHERE accountId = :accountId)", 
                    NetBanking.class)
                    .setParameter("accountId", accountId)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error retrieving NetBanking for accountId: {}", accountId, e);
            throw e; 
        }
    }
    public NetBanking getNetBankingByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM NetBanking nb WHERE nb.user.userId = :userId";
            Query<NetBanking> query = session.createQuery(hql, NetBanking.class);
            query.setParameter("userId", userId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Error retrieving NetBanking for userId: {}", userId, e);
            throw e; 
        }
    }
}
