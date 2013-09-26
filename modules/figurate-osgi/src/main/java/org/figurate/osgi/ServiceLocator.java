package org.figurate.osgi;

public interface ServiceLocator {

	/**
     * @param <T> the service type
     * @param name the service name
     * @return a service with the specified name
     * @throws ServiceNotAvailableException where the requested service is not available
     */
    <T> T findService(ServiceName name) throws ServiceNotAvailableException;
}
