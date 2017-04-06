package com.dsi.authentication.service;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.model.Tenant;

/**
 * Created by sabbir on 6/15/16.
 */
public interface TenantService {

    void saveTenant(Tenant tenant) throws CustomException;
    void updateTenant(Tenant tenant) throws CustomException;
    Tenant getTenantByID(String tenantID) throws CustomException;
}
