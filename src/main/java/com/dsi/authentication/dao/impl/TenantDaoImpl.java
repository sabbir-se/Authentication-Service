package com.dsi.authentication.dao.impl;

import com.dsi.authentication.dao.TenantDao;
import com.dsi.authentication.model.Tenant;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public class TenantDaoImpl extends BaseDao implements TenantDao {

    private static final Logger logger = Logger.getLogger(TenantDaoImpl.class);

    @Override
    public boolean saveTenant(Tenant tenant) {
        return save(tenant);
    }

    @Override
    public boolean updateTenant(Tenant tenant) {
        return update(tenant);
    }

    @Override
    public Tenant getTenantByID(String tenantID) {
        Session session = null;
        Tenant tenant = null;
        try {
            session = getSession();
            Query query = session.createQuery("FROM Tenant t WHERE t.tenantId =:tenantID AND t.isActive =:isActive");
            query.setParameter("tenantID", tenantID);
            query.setParameter("isActive", true);

            tenant = (Tenant) query.uniqueResult();

        } catch (Exception e) {
            logger.error("Database error occurs when get: " + e.getMessage());
        } finally {
            if(session != null) {
                close(session);
            }
        }
        return tenant;
    }
}
