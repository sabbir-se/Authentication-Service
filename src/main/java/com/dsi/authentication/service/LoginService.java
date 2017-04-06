package com.dsi.authentication.service;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.model.Login;

/**
 * Created by sabbir on 6/16/16.
 */
public interface LoginService {

    String saveLoginInfo(Login login) throws CustomException;
    void updateLoginInfo(Login login) throws CustomException;
    void updateLoginInfo(Login login, String userId) throws CustomException;
    void deleteLoginInfo(String userID) throws CustomException;
    Login getLoginInfo(String userID, String email) throws CustomException;
    Login getLoginInfoByResetToken(String resetToken) throws CustomException;
}
