package com.dsi.authentication.dao;

import com.dsi.authentication.model.Login;

/**
 * Created by sabbir on 6/15/16.
 */
public interface DBLoginHandlerDao {

    Login getLoginInfo(String email, String password);
}
