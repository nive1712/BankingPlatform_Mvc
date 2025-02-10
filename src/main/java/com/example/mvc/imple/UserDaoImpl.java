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
import com.example.mvc.model.User;

@Repository
public class UserDaoImpl {

    private static final String ERROR_MESSAGE = "An error occurred while updating the user.";
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BankAccountDaoImpl bankaccountdao;
    
    

    public UserDaoImpl() {
    	
    }
    @Transactional
    public int saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return user.getUserId();
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return -1;
        }
    }

    @Transactional
    public User findUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return null;
        }
    }

    @Transactional
    public User findByUsernameOrEmail(String identifier) {
        String hql = "FROM User WHERE username = :identifier OR email = :identifier";
        Query<User> query = sessionFactory.getCurrentSession().createQuery(hql, User.class);
        query.setParameter("identifier", identifier);
        return query.uniqueResult();
    }

    @Transactional
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return null;
        }
    }

    @Transactional
    public User getUserById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, userId);
        } catch (Exception e) {
            logger.error(ERROR_MESSAGE, e);
            return null;
        }
    }
}



