package org.figurate.auth

import javax.security.auth.Subject
import javax.security.auth.callback.CallbackHandler
import javax.security.auth.login.LoginException
import javax.security.auth.spi.LoginModule

/**
 * Created by fortuna on 1/07/14.
 */
class LoginModuleImpl implements LoginModule {

    def subject
    def callbackHandler

    @Override
    void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject
        this.callbackHandler = callbackHandler
    }

    @Override
    boolean login() throws LoginException {
        return true
    }

    @Override
    boolean commit() throws LoginException {
        return true
    }

    @Override
    boolean abort() throws LoginException {
        return true
    }

    @Override
    boolean logout() throws LoginException {
        return false
    }
}
