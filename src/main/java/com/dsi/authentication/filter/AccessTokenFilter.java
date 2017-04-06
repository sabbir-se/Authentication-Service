package com.dsi.authentication.filter;

import com.dsi.authentication.util.Constants;
import com.dsi.authentication.util.Utility;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by sabbir on 6/16/16.
 */
public class AccessTokenFilter implements ContainerRequestFilter {

    @Context
    HttpServletRequest request;

    private static final Logger logger = Logger.getLogger(AccessTokenFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String header = request.getHeader(Constants.AUTHORIZATION);
        String path = requestContext.getUriInfo().getPath();

        logger.info("Request Path: " + path);

        if(header != null) {
            logger.info("Header: " + header);
            String accessToken = Utility.getFinalToken(header);

            logger.info("Access Token: " + accessToken);
            request.setAttribute("access_token", accessToken);
        }
    }
}
