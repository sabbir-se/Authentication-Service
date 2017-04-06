package com.dsi.authentication.dao;

import com.dsi.authentication.model.Tenant;

/**
 * Created by sabbir on 6/15/16.
 */
public interface TenantDao {

    boolean saveTenant(Tenant tenant);
    boolean updateTenant(Tenant tenant);
    Tenant getTenantByID(String tenantID);
}
