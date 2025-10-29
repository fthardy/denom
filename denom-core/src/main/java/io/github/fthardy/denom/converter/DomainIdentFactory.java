package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.IdentType;

/**
 * TODO
 */
public sealed interface DomainIdentFactory permits AtomicIdentFactory, CompositeIdentFactory {

    /**
     * TODO
     * @return
     */
    IdentType type();
}
