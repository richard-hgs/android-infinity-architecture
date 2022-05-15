package com.infinity.architecture.utils.backservices.api.apacheutils;

import java.util.Map;

import javax.net.ssl.SSLParameters;

/**
 * A strategy allowing for a choice of an alias during SSL authentication.
 *
 * @since 4.4
 */
public interface PrivateKeyStrategy {

    /**
     * Determines what key material to use for SSL authentication.
     */
    String chooseAlias(Map<String, PrivateKeyDetails> aliases, SSLParameters sslParameters);

}
