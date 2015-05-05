package rso.core.abstraction;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by kometa on 05.05.2015.
 */
public abstract class BaseNode implements Node {

    @Value("${rso.addresses}")
    private String[] addresses;

    @Value("${rso.port}")
    private int port;

}
