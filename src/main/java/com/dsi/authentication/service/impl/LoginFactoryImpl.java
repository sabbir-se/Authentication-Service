package com.dsi.authentication.service.impl;

import com.dsi.authentication.dao.impl.BaseDao;
import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.service.LoginFactory;
import com.dsi.authentication.util.Constants;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;

/**
 * Created by sabbir on 6/15/16.
 */
public class LoginFactoryImpl implements LoginFactory {

    private static final Logger logger = Logger.getLogger(LoginFactoryImpl.class);

    @Override
    public Object getInstance(String className) throws CustomException {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> cons = clazz.getConstructor();
            return cons.newInstance();

        } catch (Exception e){
            logger.error("Failed to initialize instance: " + e.getMessage());
            ErrorContext errorContext = new ErrorContext(null, null, e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0006,
                    Constants.AUTHENTICATE_SERVICE_0006_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
