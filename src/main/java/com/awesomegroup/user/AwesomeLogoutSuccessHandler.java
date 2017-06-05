package com.awesomegroup.user;

import com.awesomegroup.general.ResponseJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by c309044 on 2017-06-05.
 */
public class AwesomeLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(ResponseJson.create().message("/login").build()));
        httpServletResponse.getWriter().flush();
    }
}
