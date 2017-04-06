package com.dsi.authentication.util;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.model.Login;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.security.SecureRandom;
import java.util.*;

/**
 * Created by sabbir on 6/13/16.
 */
public class Utility {

    private static final Logger logger = Logger.getLogger(Utility.class);

    private static final String CHAR = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
    private static final int PASSWORD_LENGTH = 8;

    public static boolean isNullOrEmpty(String s){

        if(s==null ||s.isEmpty() ){
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(List list){

        if(list==null || list.size() == 0 ){
            return true;
        }
        return false;
    }

    public static String generateRandomPassword() {
        Random random = new SecureRandom();

        String pw = "";
        for (int i=0; i<PASSWORD_LENGTH; i++)
        {
            int index = (int)(random.nextDouble()*CHAR.length());
            pw += CHAR.substring(index, index+1);
        }
        return pw;
    }

    public static String generateRandomString(){
        return UUID.randomUUID().toString();
    }

    public static Date today() {
        return new Date();
    }

    public static String validation(JSONObject requestObj, String str) throws CustomException {

        ErrorContext errorContext;
        try {
            if (requestObj.has(str)) {
                return requestObj.getString(str);

            } else {
                errorContext = new ErrorContext(str, null, "Params: '"+ str +"' are missing.");
            }
        } catch (Exception e){
            errorContext = new ErrorContext(str, null, e.getMessage());
        }
        ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0008,
                Constants.AUTHENTICATE_SERVICE_0008_DESCRIPTION, errorContext);
        throw new CustomException(errorMessage);
    }

    public static String getFinalToken(String header) {
        String[] tokenPart = header.split("[\\$\\(\\)]");
        return tokenPart[2];
    }

    public static String getTokenSecretKey(String key){
        byte[] valueDecoded = Base64.getDecoder().decode(key.getBytes());
        return new String(valueDecoded);
    }

    public static String getUserObject(Login login, String currentUserID) throws JSONException {
        JSONObject userObj = new JSONObject();
        userObj.put("firstName", login.getFirstName());
        userObj.put("lastName", login.getLastName());
        userObj.put("gender", login.getGender());
        userObj.put("email", login.getEmail());
        userObj.put("phone", login.getPhone());
        userObj.put("createBy", currentUserID);
        userObj.put("modifiedBy", currentUserID);
        userObj.put("roleId", login.getRoleId());
        userObj.put("version", 1);

        return userObj.toString();
    }

    public static String getNotificationList(JSONObject contentObj, Long templateId) throws JSONException {

        JSONArray notificationList = new JSONArray();

        JSONObject notificationObj = new JSONObject();
        notificationObj.put("notificationTypeId", Constants.NOTIFICATION_EMAIL_TYPE_ID);
        notificationObj.put("notificationTemplateId", templateId);
        notificationObj.put("systemId", Constants.SYSTEM_ID);
        notificationObj.put("contentJson", contentObj.toString());
        notificationObj.put("maxRetryCount", 5);
        notificationObj.put("processed", true);
        notificationObj.put("retryInterval", 1);

        notificationList.put(notificationObj);
        return notificationList.toString();
    }
}
