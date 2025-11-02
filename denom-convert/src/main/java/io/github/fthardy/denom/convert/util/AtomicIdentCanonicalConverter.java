package io.github.fthardy.denom.convert.util;

import io.github.fthardy.denom.AtomicIdent;

/**
 * Defines the support interface for an atomic identifiers which is a converter for converting atomic identifiers to a canonical representation and from a
 * canonical representation.
 *
 * @param <T> a concrete atomic identifier type class.
 */
public non-sealed interface AtomicIdentCanonicalConverter<T extends AtomicIdent<?>> extends DomainIdentConvertSupport<T> {

    /**
     * Convert a given atomic identifier instance into a canonical representation.
     *
     * @param atomicIdent the atomic identifier instance to be converted.
     *
     * @return the canonical representation string.
     */
    default String toCanonical(AtomicIdent<?> atomicIdent) {
        return atomicIdent.getIdentityValue().toString();
    }

    /**
     * Convert a given canonical representation string into an instance of an atomic identifier.
     *
     * @param canonical the canonical representation string.
     *
     * @return the atomic identifier instance.
     */
    T fromCanonical(String canonical);
}
