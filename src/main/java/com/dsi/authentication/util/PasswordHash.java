package com.dsi.authentication.util;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sabbir on 6/15/16.
 */
public class PasswordHash {

    private static final int iterations = 1000;
    private static final int desiredKeyLen = 64 * 8;

    public static String hash(String password, String salts) throws CustomException {
        try {
            byte[] salt = salts.getBytes();

            SecretKeyFactory f = SecretKeyFactory.getInstance("_instance_name_");
            SecretKey key = f.generateSecret(new PBEKeySpec(
                    password.toCharArray(), salt, iterations, desiredKeyLen)
            );

            byte[] hash = key.getEncoded();
            return iterations + ":" + toHex(hash);

        } catch (Exception e){
            ErrorContext errorContext = new ErrorContext(null, null, e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0011,
                    Constants.AUTHENTICATE_SERVICE_0011_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
