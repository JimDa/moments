package com.moments.auth.security;

import com.moments.auth.model.exception.UserEmailNotFoundException;
import com.moments.auth.model.exception.UserPhoneNumNotFoundException;
import com.moments.auth.service.impl.CustomUserDetailService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class CustomAuthenticationProvider extends AbstractCustomAuthenticationProvider {
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";
    private PasswordEncoder passwordEncoder;
    private volatile String userNotFoundEncodedPassword;
    private CustomUserDetailService customUserDetailService;

    public CustomAuthenticationProvider() {
        this.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, CustomAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractCustomAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractCustomAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.customUserDetailService, "A CustomUserDetailsService must be set");
    }

    @Override
    protected final UserDetails retrieveUser(String username, CustomAuthenticationToken authentication) throws AuthenticationException {
        this.prepareTimingAttackProtection();
        String type = (String) authentication.getType();
        try {
            UserDetails loadedUser;
            switch (type) {
                case "phoneNum":
                    loadedUser = this.getUserDetailsService().loadByPhoneNum(username);
                    break;
                case "email":
                    loadedUser = this.getUserDetailsService().loadByEmail(username);
                    break;
                default:
                    loadedUser = this.getUserDetailsService().loadUserByUsername(username);
                    break;
            }
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            } else {
                return loadedUser;
            }
        } catch (UsernameNotFoundException var4) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var4;
        } catch (UserPhoneNumNotFoundException var5) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var5;
        } catch (UserEmailNotFoundException var6) {
            this.mitigateAgainstTimingAttack(authentication);
            throw var6;
        } catch (InternalAuthenticationServiceException var7) {
            throw var7;
        } catch (Exception var8) {
            throw new InternalAuthenticationServiceException(var8.getMessage(), var8);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
        }

    }

    private void mitigateAgainstTimingAttack(CustomAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }

    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setUserDetailsService(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    protected CustomUserDetailService getUserDetailsService() {
        return this.customUserDetailService;
    }
}
