package com.dsi.authentication.service;

import com.dsi.authentication.exception.CustomException;
import io.jsonwebtoken.Claims;

/**
 * Created by sabbir on 6/17/16.
 */
public interface TokenService {

    String createToken(String id, String issuer, String subject, long time);
    Claims parseToken(String accessToken) throws CustomException;
}
