package com.dsi.authentication.dao.impl;

import com.dsi.authentication.util.SessionUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public class BaseDao {

    private static final Logger logger = Logger.getLogger(BaseDao.class);

    public Session getSession(){
        Session session = SessionUtil.getSession();
        session.beginTransaction();
        return session;
    }

    public void close(Session session){
        session.getTransaction().commit();
        session.close();
    }

    public boolean save(Object object) {
        Session session = null;
        boolean success = true;
        try {
            session = getSession();
            session.save(object);

        } catch (Exception e) {
            logger.error("Database error occurs when save: " + e.getMessage());
            success = false;

        } finally {
            if(session != null) {
                close(session);
            }
        }
        return success;
    }

    public boolean update(Object object) {
        Session session = null;
        boolean success = true;
        try {
            session = getSession();
            session.update(object);

        } catch (Exception e) {
            logger.error("Database error occurs when update: " + e.getMessage());
            success = false;

        } finally {
            if(session != null) {
                close(session);
            }
        }
        return success;
    }

    public boolean delete(Object object) {
        Session session = null;
        boolean success = true;
        try {
            session = getSession();
            session.delete(object);

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
}
