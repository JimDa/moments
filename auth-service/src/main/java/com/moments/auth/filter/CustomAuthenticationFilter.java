package com.moments.auth.filter;

import com.moments.auth.security.CustomAuthenticationToken;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "principal";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "credential";
    public static final String SPRING_SECURITY_FORM_TYPE_KEY = "type";
    private String principalParameter = "principal";
    private String credentialParameter = "credential";
    private String typeParameter = "credential";
    private boolean postOnly = true;

    public CustomAuthenticationFilter() {
        super(new AntPathRequestMatcher("/custom/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String type = this.obtainType(request);
            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            if (type == null) {
                type = "";
            }

            username = username.trim();
            CustomAuthenticationToken authRequest = new CustomAuthenticationToken(username, password, type);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    private String obtainType(HttpServletRequest request) {
        return request.getParameter(this.typeParameter);
    }


    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.principalParameter);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.credentialParameter);
    }

    protected void setDetails(HttpServletRequest request, CustomAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "principal parameter must not be empty or null");
        this.principalParameter = usernameParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "credential parameter must not be empty or null");
        this.credentialParameter = passwordParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.principalParameter;
    }

    public final String getPasswordParameter() {
        return this.credentialParameter;
    }
}
