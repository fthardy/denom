package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.IdentType;

import java.util.*;

/**
 * A simple provider implementation which holds a set of {@link DomainIdentFactory} instances mapped by their {@link DomainIdentFactory#type() supported type}.
 */
public final class DomainIdentFactoryProvider {

    private final Map<IdentType, DomainIdentFactory> factories;

    /**
     * Creates an instance of this factory provider. The available factory instances are obtained through {@link ServiceLoader}.
     */
    public DomainIdentFactoryProvider() {
        this(ServiceLoader.load(DomainIdentFactory.class));
    }

    /**
     * Creates an instance of this factory provider.
     *
     * @param factories the available factory instances.
     */
    public DomainIdentFactoryProvider(Iterable<? extends DomainIdentFactory> factories) {
        Map<IdentType, DomainIdentFactory> mappedProviders = new HashMap<>();
        for (DomainIdentFactory provider : factories) {
            if (mappedProviders.containsKey(provider.type())) {
                throw new IllegalArgumentException("Duplicate type: " + provider.type());
            }
            mappedProviders.put(provider.type(), provider);
        }
        this.factories = Collections.unmodifiableMap(mappedProviders);
    }

    /**
     * Get a particular {@code DomainIdentProvider}-Instance.
     * <p>
     * The requested factory must exist otherwise an exception is thrown.
     * </p>
     *
     * @param identType the type of the domain identifier.
     *
     * @return the provider instance.
     *
     * @throws NoSuchElementException when no provider exists for the given type.
     */
    public DomainIdentFactory getDomainIdentFactory(IdentType identType) {
        if (this.factories.containsKey(identType)) {
            return this.factories.get(identType);
        }
        throw new NoSuchElementException("No factory for domain identifier type: " + identType);
    }
}
