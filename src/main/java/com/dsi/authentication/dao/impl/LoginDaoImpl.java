package com.dsi.authentication.dao.impl;

import com.dsi.authentication.dao.LoginDao;
import com.dsi.authentication.model.Login;
import com.dsi.authentication.util.Utility;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/16/16.
 */
public class LoginDaoImpl extends BaseDao implements LoginDao {

    private static final Logger logger = Logger.getLogger(LoginDaoImpl.class);

    @Override
    public boolean saveLoginInfo(Login login) {
        return save(login);
    }

    @Override
    public boolean updateLoginInfo(Login login) {
        return update(login);
    }

    @Override
    public boolean deleteLoginInfo(String userID) {
        Session session = null;
        boolean success = true;
        try {
            session = getSession();
            Query query = session.createQuery("DELETE FROM Login l WHERE l.userId =:userID");
            query.setParameter("userID", userID);

            success = query.executeUpdate() > 0;

        } catch (Exception e) {
            logger.error("Database error occurs when delete: " + e.getMessage());
            success = false;

        } finally {
            if(session != null) {
                close(session);
            }
        }
        return success;
    }

    @Override
    public Login getLoginInfo(String userID, String email) {
        Session session = null;
        Login login = null;
        try{
            session = getSession();
            Query query;

            if(userID == null){
                query = session.createQuery("FROM Login l WHERE l.email =:email");
                query.setParameter("email", email);

            }else{
                query = session.createQuery("FROM Login l WHERE l.userId =:userID");
                query.setParameter("userID", userID);
            }
            login = (Login) query.uniqueResult();

        } catch (Exception e){
            logger.error("Database error occurs when get: " + e.getMessage());
        } finally {
            if(session != null) {
                close(session);
            }
        }
        return login;
    }

    @Override
    public Login getLoginInfoByResetToken(String resetToken) {
        Session session = null;
        Login login = null;
        try{
            session = getSession();
            Query query = session.createQuery("FROM Login l WHERE l.resetPasswordToken =:resetToken AND l.resetTokenExpireTime >=:curDate");
            query.setParameter("resetToken", resetToken);
            query.setParameter("curDate", Utility.today());

            login = (Login) query.uniqueResult();

        } catch (Exception e){
            logger.error("Database error occurs when get: " + e.getMessage());
        } finally {
            if(session != null) {
                close(session);
            }
        }
        return login;
    }
}
