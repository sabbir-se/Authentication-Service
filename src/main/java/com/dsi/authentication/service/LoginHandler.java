package com.dsi.authentication.service;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.model.Login;

/**
 * Created by sabbir on 6/15/16.
 */
public interface LoginHandler {

    Login validateUser(String username, String password) throws CustomException;
}
