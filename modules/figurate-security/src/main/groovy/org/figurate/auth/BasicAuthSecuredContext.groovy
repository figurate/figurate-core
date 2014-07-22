package org.figurate.auth

import org.apache.commons.codec.binary.Base64
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.osgi.service.component.ComponentContext
import org.osgi.service.http.HttpContext

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by fortuna on 6/07/14.
 */
@Component(immediate = true)
@Service(value = HttpContext)
@org.apache.felix.scr.annotations.Properties([
    @Property(name = 'service.pid', value = 'org.figurate.httpcontext.basicauth'),
    @Property(name = 'contextId', value = 'org.figurate.context.basicauth', propertyPrivate = true),
    @Property(name = 'context.shared', value = 'true', propertyPrivate = true)
])
class BasicAuthSecuredContext implements HttpContext {

    @Property(value = 'figurate')
    private static final PROP_REALM = 'realm'

    String realm

    @Activate
    void activate(ComponentContext context) {
        realm = context.getProperties()['realm']
    }

    @Override
    boolean handleSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!request.getScheme().equals("https")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        if (request.getHeader("Authorization") == null) {
            response.setHeader('WWW-Authenticate', "Basic realm=\"$realm\"")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        if (authenticated(request)) {
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    protected boolean authenticated(HttpServletRequest request) {
        String authzHeader = request.getHeader("Authorization");
        String usernameAndPassword = new String(Base64.decodeBase64(authzHeader.substring(6).getBytes()));

        int userNameIndex = usernameAndPassword.indexOf(":");
        String username = usernameAndPassword.substring(0, userNameIndex);
        String password = usernameAndPassword.substring(userNameIndex + 1);
        // Now, do the authentication against in the way you want, ex: ldap, db stored uname/pw
        // Here I will do lame hard coded credential check. HIGHLY NOT RECOMMENDED!
        return username.equals("username") && password.equals("password")
    }

    @Override
    URL getResource(String name) {
        return null
    }

    @Override
    String getMimeType(String name) {
        return null
    }
}
