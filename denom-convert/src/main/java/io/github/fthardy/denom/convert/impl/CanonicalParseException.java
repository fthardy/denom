package io.github.fthardy.denom.convert.impl;

/**
 * Is thrown by {@link DefaultDomainIdentCanonicalConverter#fromCanonical(String)}.
 */
public final class CanonicalParseException extends RuntimeException {

    public CanonicalParseException(String message) {
        super(message);
    }

    public CanonicalParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
