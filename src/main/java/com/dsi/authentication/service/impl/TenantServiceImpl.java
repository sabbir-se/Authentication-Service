package com.dsi.authentication.service.impl;

import com.dsi.authentication.dao.TenantDao;
import com.dsi.authentication.dao.impl.TenantDaoImpl;
import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.model.Tenant;
import com.dsi.authentication.service.TenantService;
import com.dsi.authentication.util.Constants;

/**
 * Created by sabbir on 6/15/16.
 */
public class TenantServiceImpl implements TenantService {

    private static final TenantDao tenantDao = new TenantDaoImpl();

    @Override
    public void saveTenant(Tenant tenant) throws CustomException {
        validateInputForCreation(tenant);

        boolean res = tenantDao.saveTenant(tenant);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Tenant", "Tenant create failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0002,
                    Constants.AUTHENTICATE_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    private void validateInputForCreation(Tenant tenant) throws CustomException {
        if(tenant.getAuthHandler() == null){
            ErrorContext errorContext = new ErrorContext("AuthHandlerID", "Tenant",
                    "Auth handler not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0001,
                    Constants.AUTHENTICATE_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateTenant(Tenant tenant) throws CustomException {
        boolean res = tenantDao.updateTenant(tenant);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Tenant", "Tenant update failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0003,
                    Constants.AUTHENTICATE_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public Tenant getTenantByID(String tenantID) throws CustomException {
        Tenant tenant = tenantDao.getTenantByID(tenantID);
        if(tenant == null){
            ErrorContext errorContext = new ErrorContext(null, "Tenant", "Tenant not found by tenantID: " + tenantID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0005,
                    Constants.AUTHENTICATE_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        return tenant;
    }
}
