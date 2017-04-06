package com.dsi.authentication.service.impl;

import com.dsi.authentication.dao.DBLoginHandlerDao;
import com.dsi.authentication.dao.impl.DBLoginHandlerDaoImpl;
import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.model.Login;
import com.dsi.authentication.service.LoginHandler;
import com.dsi.authentication.util.Constants;

/**
 * Created by sabbir on 6/15/16.
 */
public class DBLoginHandlerImpl implements LoginHandler {

    private static final DBLoginHandlerDao dao = new DBLoginHandlerDaoImpl();

    @Override
    public Login validateUser(String username, String password) throws CustomException {
        Login login = dao.getLoginInfo(username, password);
        if(login == null){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info not found by username: "
                    + username + " & password: " + password);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0005,
                    Constants.AUTHENTICATE_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        return login;
    }
}
