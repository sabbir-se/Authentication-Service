package com.dsi.authentication.resource;

import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.model.Login;
import com.dsi.authentication.model.Tenant;
import com.dsi.authentication.service.*;
import com.dsi.authentication.service.impl.*;
import com.dsi.authentication.util.Constants;
import com.dsi.authentication.util.Utility;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import io.jsonwebtoken.Claims;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sabbir on 6/16/16.
 */

@Path("/v1/login_session")
@Api(value = "/Authentication", description = "Operations about Authentication")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LoginResource {

    private static final Logger logger = Logger.getLogger(LoginResource.class);

    private static final LoginService loginService = new LoginServiceImpl();
    private static final TenantService tenantService = new TenantServiceImpl();
    private static final LoginFactory loginFactory = new LoginFactoryImpl();
    private static final TokenService tokenService = new TokenServiceImpl();
    private static final CallAnotherResource callAnotherService = new CallAnotherResource();

    @Context
    HttpServletRequest request;

    @POST
    @ApiOperation(value = "Start Login Session", notes = "Start Login Session", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login success"),
            @ApiResponse(code = 500, message = "Login failed, unauthorized.")
    })
    public Response startLoginSession(String requestBody) throws CustomException {
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj;

        try {
            logger.info("Request Body:: " + requestBody);
            requestObj = new JSONObject(requestBody);
            String username = Utility.validation(requestObj, "username");
            String password = Utility.validation(requestObj, "password");
            String tenantID = Utility.validation(requestObj, "tenant_id");

            Tenant tenant = tenantService.getTenantByID(tenantID);
            String authHandlerClassName = tenant.getAuthHandler().getTypeImpl();
            logger.info("Auth handler class name: " + authHandlerClassName);

            LoginHandler loginHandler = (LoginHandler) loginFactory.getInstance(authHandlerClassName);
            Login login = loginHandler.validateUser(username, password);
            logger.info("Login successfully.");

            responseObj.put("tenant_name", tenant.getName());
            responseObj.put("login_id", login.getLoginId());
            responseObj.put("first_name", login.getFirstName());
            responseObj.put("last_name", login.getLastName());
            responseObj.put("user_id", login.getUserId());
            responseObj.put("username", login.getEmail());

            JSONObject resultObj = callAnotherService.getRequest(APIProvider.API_USER_CONTEXT + login.getUserId());
            String context = resultObj.has("context") ? resultObj.getString("context") : null;
            responseObj.put("context", context);

            String accessToken = tokenService.createToken(login.getUserId(), tenant.getName(),
                    responseObj.toString(), Constants.TIME_INTERVAL);

            logger.info("Generated AccessToken: " + accessToken);
            responseObj.put("access_token", accessToken);

            JSONObject userSessionObj = new JSONObject();
            userSessionObj.put("userId", login.getUserId());
            userSessionObj.put("createBy", login.getUserId());
            userSessionObj.put("modifiedBy", login.getUserId());
            userSessionObj.put("accessToken", accessToken);
            resultObj = callAnotherService.sendPost(APIProvider.API_USER_SESSION, userSessionObj.toString());
            if(resultObj.has("roleName")){
                responseObj.put("roleName", resultObj.getString("roleName"));
            }

            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @ApiOperation(value = "Delete User Session", notes = "Delete User Session", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout success"),
            @ApiResponse(code = 500, message = "Logout failed, unauthorized.")
    })
    public Response deleteUserSession() throws CustomException {
        String accessToken = request.getAttribute("access_token") != null ?
                request.getAttribute("access_token").toString() : null;

        JSONObject responseObj = new JSONObject();

        try {
            Claims parseToken = tokenService.parseToken(accessToken);

            JSONObject bodyObj = new JSONObject();
            bodyObj.put("userId", parseToken.getId());
            bodyObj.put("accessToken", accessToken);
            callAnotherService.sendDelete(APIProvider.API_USER_SESSION, bodyObj.toString());

            responseObj.put(Constants.MESSAGE, "Delete user session success");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @GET
    @ApiOperation(value = "Get Login Info", notes = "Get Login Info", position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get login info"),
            @ApiResponse(code = 500, message = "Get login info failed, unauthorized.")
    })
    public Response getLoginSession() throws CustomException {
        String accessToken = request.getAttribute("access_token") != null ?
                request.getAttribute("access_token").toString() : null;

        JSONObject responseObj;

        try {
            Claims parseToken = tokenService.parseToken(accessToken);

            JSONObject bodyObj = new JSONObject();
            bodyObj.put("userId", parseToken.getId());
            bodyObj.put("accessToken", accessToken);
            callAnotherService.sendPost(APIProvider.API_USER_SESSION_VALID, bodyObj.toString());

            String subject = parseToken.getSubject();
            logger.info("ParseToken Subject: " + subject);

            responseObj = new JSONObject(subject);
            logger.info("Login info: " + responseObj.toString());
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @POST
    @Path("/create")
    @ApiOperation(value = "Create Login Session", notes = "Create Login Session", position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Create Login success"),
            @ApiResponse(code = 500, message = "Create Login failed, unauthorized.")
    })
    public Response createLoginSession(Login login) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try {
            logger.info("Login create:: start");
            String currentUserID = login.getCreateBy();
            JSONObject resultObj = callAnotherService.sendPost(APIProvider.API_USER,
                    Utility.getUserObject(login, currentUserID));

            login.setUserId(resultObj.getString("user_id"));
            String password = loginService.saveLoginInfo(login);
            logger.info("Login create:: end");

            responseObj.put("user_id", login.getUserId());
            responseObj.put("role_name", resultObj.getString("role_name"));
            responseObj.put("password", password);
            responseObj.put(Constants.MESSAGE, "Create login success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je) {
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @PUT
    @Path("/update/{user_id}")
    @ApiOperation(value = "Update Login Session", notes = "Update Login Session", position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update Login success"),
            @ApiResponse(code = 500, message = "Update Login failed, unauthorized.")
    })
    public Response updateLoginSession(@PathParam("user_id") String userId, Login login) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try {
            logger.info("Login update:: start");
            loginService.updateLoginInfo(login, userId);
            logger.info("Login update:: end");

            responseObj.put(Constants.MESSAGE, "Update login success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je) {
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @Path("/delete/{user_id}")
    @ApiOperation(value = "Delete Login Info", notes = "Delete Login Info", position = 6)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete login info success"),
            @ApiResponse(code = 500, message = "Delete login info failed, unauthorized.")
    })
    public Response deleteLogin(@PathParam("user_id") String userID) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try {
            logger.info("Login delete:: start");
            loginService.deleteLoginInfo(userID);
            logger.info("Login delete:: end");

            responseObj.put(Constants.MESSAGE, "Delete login info success");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0009,
                    Constants.AUTHENTICATE_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
