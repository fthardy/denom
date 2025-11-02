package io.github.fthardy.denom.convert.util;

import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;

/**
 * Defines the support interface for composite identifiers which is a factory for creating composite identifier instances.
 *
 * @param <T> a concrete composite identifier type class.
 */
public non-sealed interface CompositeIdentFactory<T extends CompositeIdent> extends DomainIdentConvertSupport<T> {

    /**
     * Creates a composite identifier from a list of domain identifiers.
     *
     * @param first the first identifier instance.
     * @param second the second identifier instance.
     * @param further any further identifier instances.
     *
     * @return the new composite identifier instance.
     */
    T fromComponents(DomainIdent first, DomainIdent second, DomainIdent... further);
}
