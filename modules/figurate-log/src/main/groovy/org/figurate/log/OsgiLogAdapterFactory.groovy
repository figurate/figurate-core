package org.figurate.log

import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Service
import org.osgi.service.log.LogService

/**
 * Created by fortuna on 4/02/14.
 */
@Component(immediate = true)
@Service(value = LogAdapterFactory)
class OsgiLogAdapterFactory implements LogAdapterFactory<LogService> {

    @Override
    LogAdapter newInstance(LogService logService) {
        return new OsgiLogAdapter(logService)
    }
}
