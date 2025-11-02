package io.github.fthardy.denom.convert.util;

import io.github.fthardy.denom.DomainIdent;

/**
 * The common base interface for any converter support type.
 *
 * @param <T> a concrete domain identifier type class.
 *
 * @see DomainIdentCanonicalConverterSupportRegistry
 */
public sealed interface DomainIdentConvertSupport<T extends DomainIdent> permits AtomicIdentCanonicalConverter, CompositeIdentFactory {

    /**
     * Provides the binding of the domain identifier implementation type class with a type alias name.
     *
     * @return the binding instance.
     */
    IdentTypeClass2AliasBinding<T> typeClass2AliasBinding();
}
