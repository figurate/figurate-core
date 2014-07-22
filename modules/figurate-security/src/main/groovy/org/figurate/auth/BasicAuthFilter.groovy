package org.figurate.auth

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service

import javax.servlet.*

/**
 * Created by fortuna on 7/07/14.
 */
@Component(immediate = true, metatype = true)
@Service(value = Filter)
@org.apache.felix.scr.annotations.Properties([
        @Property(name = 'pattern', value = '/requestlog.*'),
        @Property(name = 'contextId', value = 'org.figurate.context.basicauth', propertyPrivate = true)
])
class BasicAuthFilter implements Filter {

    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response)
    }

    @Override
    void destroy() {

    }
}
