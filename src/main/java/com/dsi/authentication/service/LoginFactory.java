package com.dsi.authentication.service;

import com.dsi.authentication.exception.CustomException;

/**
 * Created by sabbir on 6/15/16.
 */
public interface LoginFactory {

    Object getInstance(String className) throws CustomException;
}
