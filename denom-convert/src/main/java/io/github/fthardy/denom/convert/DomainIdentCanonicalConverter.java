package io.github.fthardy.denom.convert;

import io.github.fthardy.denom.DomainIdent;

/**
 * Represents a converter which can convert a domain identifier to and from a canonical string representation.
 */
public interface DomainIdentCanonicalConverter {

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
