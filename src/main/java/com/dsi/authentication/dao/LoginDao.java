package com.dsi.authentication.dao;

import com.dsi.authentication.model.Login;

/**
 * Created by sabbir on 6/16/16.
 */
public interface LoginDao {

    boolean saveLoginInfo(Login login);
    boolean updateLoginInfo(Login login);
    boolean deleteLoginInfo(String userID);
    Login getLoginInfo(String userID, String email);
    Login getLoginInfoByResetToken(String resetToken);
}
