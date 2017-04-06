package com.dsi.authentication.service.impl;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.service.TokenService;
import com.dsi.authentication.util.Constants;
import com.dsi.authentication.util.Utility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * Created by sabbir on 6/15/16.
 */
public class TokenServiceImpl implements TokenService {

    private static final Logger logger = Logger.getLogger(TokenServiceImpl.class);

    @Override
    public String createToken(String id, String issuer, String subject, long time) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Utility.getTokenSecretKey(Constants.SECRET_KEY));
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }

    @Override
    public Claims parseToken(String accessToken) throws CustomException {
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(Utility.getTokenSecretKey(Constants.SECRET_KEY)))
                    .parseClaimsJws(accessToken).getBody();

        } catch (Exception e){
            logger.error("Failed to parse token: " + e.getMessage());
            ErrorContext errorContext = new ErrorContext(null, null, "Token parse failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0007,
                    Constants.AUTHENTICATE_SERVICE_0007_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
