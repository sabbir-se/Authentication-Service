package com.dsi.authentication.service.impl;

import com.dsi.authentication.dao.LoginDao;
import com.dsi.authentication.dao.impl.LoginDaoImpl;
import com.dsi.authentication.exception.CustomException;
import com.dsi.authentication.exception.ErrorContext;
import com.dsi.authentication.exception.ErrorMessage;
import com.dsi.authentication.model.Login;
import com.dsi.authentication.service.LoginService;
import com.dsi.authentication.util.Constants;
import com.dsi.authentication.util.PasswordHash;
import com.dsi.authentication.util.Utility;
import org.apache.log4j.Logger;

/**
 * Created by sabbir on 6/16/16.
 */
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class);

    private static final LoginDao dao = new LoginDaoImpl();

    @Override
    public String saveLoginInfo(Login login) throws CustomException {
        validateInputForCreation(login);

        login.setCreatedDate(Utility.today());
        login.setModifiedDate(Utility.today());

        String password = Utility.generateRandomPassword();
        logger.info("Password: " + password);
        login.setSalt(Constants.SALT);
        login.setPassword(PasswordHash.hash(password, Constants.SALT));

        boolean res = dao.saveLoginInfo(login);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info save failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0003,
                    Constants.AUTHENTICATE_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        return password;
    }

    private void validateInputForCreation(Login login) throws CustomException {
        if(login.getEmail() == null){
            ErrorContext errorContext = new ErrorContext(null, "Login",
                    "Email not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0001,
                    Constants.AUTHENTICATE_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        if(login.getUserId() == null){
            ErrorContext errorContext = new ErrorContext(null, "Login",
                    "UserID not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0001,
                    Constants.AUTHENTICATE_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        if(dao.getLoginInfo(null, login.getEmail()) != null){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info already exist.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0001,
                    Constants.AUTHENTICATE_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateLoginInfo(Login login) throws CustomException {
        boolean res = dao.updateLoginInfo(login);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info update failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0003,
                    Constants.AUTHENTICATE_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateLoginInfo(Login login, String userId) throws CustomException {
        Login existLogin = dao.getLoginInfo(userId, null);
        if(existLogin == null){
            ErrorContext errorContext = new ErrorContext(null, "Login",
                    "Login info not found by userID: " + userId);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0005,
                    Constants.AUTHENTICATE_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        existLogin.setFirstName(login.getFirstName());
        existLogin.setLastName(login.getLastName());
        existLogin.setActive(login.isActive());
        existLogin.setModifiedBy(login.getModifiedBy());
        existLogin.setModifiedDate(Utility.today());
        boolean res = dao.updateLoginInfo(existLogin);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info update failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0003,
                    Constants.AUTHENTICATE_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteLoginInfo(String userID) throws CustomException {
        boolean res = dao.deleteLoginInfo(userID);
        if(!res){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info delete failed.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0004,
                    Constants.AUTHENTICATE_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public Login getLoginInfo(String userID, String email) throws CustomException {
        Login login = dao.getLoginInfo(userID, email);
        if(login == null){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info not found by userID: "
                    + userID + " & email: " + email);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0005,
                    Constants.AUTHENTICATE_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        return login;
    }

    @Override
    public Login getLoginInfoByResetToken(String resetToken) throws CustomException {
        Login login = dao.getLoginInfoByResetToken(resetToken);
        if(login == null){
            ErrorContext errorContext = new ErrorContext(null, "Login", "Login info not found by passwordResetToken: " + resetToken);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHENTICATE_SERVICE_0005,
                    Constants.AUTHENTICATE_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        return login;
    }
}
