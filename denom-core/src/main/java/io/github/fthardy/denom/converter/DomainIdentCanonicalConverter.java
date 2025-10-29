package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.IdentType;

/**
 * Represents a converter which can convert a domain identifier to and from a canonical string representation.
 * <p>
 * An instance of this interface type is used internally by {@link DomainIdent} to convert any domain identifier instances into a canonical string
 * representation (via {@link DomainIdent#toCanonical()}) and to reconvert such a canonical representation back into a domain identifier instance (via
 * {@link DomainIdent#fromCanonical(String)}).
 * </p>
 * <p>
 * A default implementation is used as long it is not replaced by a custom implementation. A custom implementation must be provided as a service implementation
 * instance via META-INF/services (see {@link java.util.ServiceLoader}). However, only one implementation is allowed to be on the class path. If there is more
 * than one implementation available the system will fall back to the default implementation.
 * </p>
 * <p>
 * Typically, there should be no reason to provide a custom implementation.
 * </p>
 *
 * @see DomainIdent
 */
public interface DomainIdentCanonicalConverter {

    /**
     * Check whether the given type is supported by the converter.
     *
     * @param type the type to check.
     */
    void assertSupports(IdentType type);

    /**
     * Converts a canonical string representation of a domain identifier into the identifier model representation.
     *
     * @param canonical the canonical string which presumably represents a domain identifier.
     *
     * @return a new domain identifier instance.
     */
    DomainIdent fromCanonical(String canonical);

    /**
     * Converts a domain identifier instance into a canonical string representation.
     *
     * @param domainIdent the domain identifier instance to be converted.
     *
     * @return the canonical string representation of the given domain identifier.
     */
    String toCanonical(DomainIdent domainIdent);
}
