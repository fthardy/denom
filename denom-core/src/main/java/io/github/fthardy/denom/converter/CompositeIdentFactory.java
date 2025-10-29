package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;

/**
 * TODO
 */
public non-sealed interface CompositeIdentFactory extends DomainIdentFactory {

    /**
     * TODO
     * @param first
     * @param second
     * @param further
     * @return
     */
    CompositeIdent createFrom(DomainIdent first, DomainIdent second, DomainIdent... further);
}
