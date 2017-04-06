package com.dsi.authentication.dao.impl;

import com.dsi.authentication.dao.DBLoginHandlerDao;
import com.dsi.authentication.model.Login;
import com.dsi.authentication.util.PasswordHash;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public class DBLoginHandlerDaoImpl extends BaseDao implements DBLoginHandlerDao {

    private static final Logger logger = Logger.getLogger(DBLoginHandlerDaoImpl.class);

    @Override
    public Login getLoginInfo(String email, String password) {
        Session session = null;
        Login login = null;
        try {
            session = getSession();
            Query query = session.createQuery("FROM Login l WHERE l.email =:email AND l.isActive =:active");
            query.setParameter("email", email);
            query.setParameter("active", true);

            login = (Login) query.uniqueResult();
            if(login != null){
                String hash = PasswordHash.hash(password, login.getSalt());
                if(!hash.equals(login.getPassword())){
                    login = null;
                }
            }
        } catch (Exception e) {
            logger.error("Database error occurs when get: " + e.getMessage());
        } finally {
            if(session != null) {
                close(session);
            }
        }
        return login;
    }
}
