package com.dsi.authentication.service.impl;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.util.Constants;
import com.dsi.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by sabbir on 1/10/17.
 */
public class CallAnotherResource {

    private static final Logger logger = Logger.getLogger(CallAnotherResource.class);
    private static final HttpClient httpClient = new HttpClient();

    public JSONObject sendPost(String url, String bodyObject) throws CustomException {
        logger.info("Request body:: " + bodyObject);
        String result = httpClient.sendPost(url, bodyObject, Constants.SYSTEM, Constants.SYSTEM_HEADER_ID);
        logger.info(url + " api call result:: " + result);

        return getResultObj(result);
    }

    public JSONObject sendPut(String url, String bodyObject) throws CustomException {
        logger.info("Request body:: " + bodyObject);
        String result = httpClient.sendPut(url, bodyObject, Constants.SYSTEM, Constants.SYSTEM_HEADER_ID);
        logger.info(url + " api call result:: " + result);

        return getResultObj(result);
    }

    public JSONObject sendDelete(String url, String bodyObject) throws CustomException {
        String result = httpClient.sendDelete(url, bodyObject, Constants.SYSTEM, Constants.SYSTEM_HEADER_ID);
        logger.info(url + " api call result:: " + result);

        return getResultObj(result);
    }

    public JSONObject getRequest(String url) throws CustomException {
        String result = httpClient.getRequest(url, Constants.SYSTEM, Constants.SYSTEM_HEADER_ID);
        logger.info(url + " api call result:: " + result);

        return getResultObj(result);
    }

    private JSONObject getResultObj(String result) throws CustomException {
        JSONObject resultObj;
        try{
            resultObj = new JSONObject(result);
            if (!resultObj.has(Constants.MESSAGE)) {
                ErrorContext errorContext = new ErrorContext(null, null, "Another api call failed.");
                ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0012,
                        Constants.AUTHENTICATE_SERVICE_0012_DESCRIPTION, errorContext);
                throw new CustomException(errorMessage);
            }
            return resultObj;

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
