package com.infinity.architecture.utils.backservices.api.apacheutils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * {@link javax.net.ssl.SSLContext} factory methods.
 *
 * <p>
 * Please note: the default Oracle JSSE implementation of
 * {@link SSLContext#init(javax.net.ssl.KeyManager[], javax.net.ssl.TrustManager[], java.security.SecureRandom)
 * SSLContext#init(KeyManager[], TrustManager[], SecureRandom)}
 * accepts multiple key and trust managers, however only only first matching type is ever used.
 * See for example:
 * <a href="http://docs.oracle.com/javase/7/docs/api/javax/net/ssl/SSLContext.html#init%28javax.net.ssl.KeyManager[],%20javax.net.ssl.TrustManager[],%20java.security.SecureRandom%29">
 * SSLContext.html#init
 * </a>
 * @since 4.4
 */
public final class SSLContexts {

    private SSLContexts() {
        // Do not allow utility class to be instantiated.
    }

    /**
     * Creates default factory based on the standard JSSE trust material
     * ({@code cacerts} file in the security properties directory). System properties
     * are not taken into consideration.
     *
     * @return the default SSL socket factory
     * @throws SSLInitializationException if NoSuchAlgorithmException or KeyManagementException
     * are thrown when invoking {@link SSLContext#getInstance(String)}
     */
    public static SSLContext createDefault() throws SSLInitializationException {
        try {
            final SSLContext sslContext = SSLContext.getInstance(SSLContextBuilder.TLS);
            sslContext.init(null, null, null);
            return sslContext;
        } catch (final NoSuchAlgorithmException | KeyManagementException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
    }

    /**
     * Creates default SSL context based on system properties. This method obtains
     * default SSL context by calling {@code SSLContext.getInstance("Default")}.
     * Please note that {@code Default} algorithm is supported as of Java 6.
     * This method will fall back onto {@link #createDefault()} when
     * {@code Default} algorithm is not available.
     *
     * @return default system SSL context
     * @throws SSLInitializationException if {@link #createDefault()} throws it
     */
    public static SSLContext createSystemDefault() throws SSLInitializationException {
        try {
            return SSLContext.getDefault();
        } catch (final NoSuchAlgorithmException ex) {
            return createDefault();
        }
    }

    /**
     * Creates custom SSL context.
     *
     * @return default system SSL context
     */
    public static SSLContextBuilder custom() {
        return SSLContextBuilder.create();
    }

}