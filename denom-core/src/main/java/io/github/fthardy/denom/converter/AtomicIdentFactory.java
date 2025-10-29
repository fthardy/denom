package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.AtomicIdent;

/**
 * TODO
 */
public non-sealed interface AtomicIdentFactory extends DomainIdentFactory {

    /**
     * TODO
     * @param identifierSequence
     * @return
     */
    AtomicIdent createFrom(String identifierSequence);
}
