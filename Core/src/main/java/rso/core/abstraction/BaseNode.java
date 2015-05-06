package rso.core.abstraction;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by kometa on 05.05.2015.
 */
public abstract class BaseNode implements Node {

    @Value("${rso.addresses.middleware}")
    protected String[] addressesMiddleware;

    @Value("${rso.addresses.server}")
    protected String[] addressesServer;

    @Value("${rso.port.internal}")
    private int portInternal;

    @Value("${rso.port.external}")
    private int portExternal;
}
