package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.AtomicIdent;
import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * The default implementation of a domain identifier converter which converts the domain identifier instances into a human-readable string representation and
 * reconverts an identifier vice versa from this representation back into its model representation.
 */
public final class DefaultDomainIdentCanonicalConverter implements DomainIdentCanonicalConverter {

    private static final char ATOMIC_BEGIN = '(';
    private static final char ATOMIC_END = ')';
    private static final char COMPOSITE_BEGIN = '[';
    private static final char COMPOSITE_END = ']';
    private static final String COMPONENT_SEPARATOR = ",";

    private final BiFunction<String, String, AtomicIdent> atomicIdentFactory;
    private final BiFunction<String, DomainIdent[], CompositeIdent> compositeIdentFactory;

    public DefaultDomainIdentCanonicalConverter() {
        this(AtomicIdent::createInstance, (typeId, components) -> //
                CompositeIdent.createInstance(typeId, components[0], components[1], Arrays.copyOfRange(components, 2, components.length)));
    }

    // facilitates testability
    DefaultDomainIdentCanonicalConverter(BiFunction<String, String, AtomicIdent> atomicIdentFactory,
                                         BiFunction<String, DomainIdent[], CompositeIdent> compositeIdentFactory) {
        this.atomicIdentFactory = atomicIdentFactory;
        this.compositeIdentFactory = compositeIdentFactory;
    }

    @Override
    public String toCanonical(DomainIdent domainIdent) {
        return switch (domainIdent) {
            case AtomicIdent atomicIdent -> //
                    "%s%s%s%s".formatted(domainIdent.typeId(), //
                            ATOMIC_BEGIN, //
                            UriCodec.encodeUriConform( //
                                    atomicIdent.getIdentitySequence(), //
                                    COMPONENT_SEPARATOR+ATOMIC_BEGIN+ATOMIC_END+COMPOSITE_BEGIN+COMPOSITE_END), //
                            ATOMIC_END);
            case CompositeIdent compositeIdent -> //
                    "%s%s%s%s".formatted( //
                            domainIdent.typeId(), //
                            COMPOSITE_BEGIN, compositeIdent.components().stream() //
                                    .map(this::toCanonical) // recursive call for each component!
                                    .collect(Collectors.joining(COMPONENT_SEPARATOR)), //
                            COMPOSITE_END);
        };
    }

    @Override
    public DomainIdent fromCanonical(String canonical) {
        int typeNameEndIndex = canonical.indexOf(COMPOSITE_BEGIN);
        if (typeNameEndIndex > 0) { // presumably a composite identifier
            if (canonical.endsWith("" + COMPOSITE_END)) {
                DomainIdent[] components = Arrays.stream(canonical.substring(typeNameEndIndex + 1, canonical.length() - 1).split(COMPONENT_SEPARATOR)) //
                        .map(this::fromCanonical).toList().toArray(new DomainIdent[0]);
                if (components.length > 1) {
                    return compositeIdentFactory.apply(canonical.substring(0, typeNameEndIndex), components);
                }
            }
        } else { // presumably an atomic identifier
            typeNameEndIndex = canonical.indexOf(ATOMIC_BEGIN);
            if (typeNameEndIndex > 0) {
                if (canonical.endsWith("" + ATOMIC_END)) {
                    return atomicIdentFactory.apply( //
                            canonical.substring(0, typeNameEndIndex), //
                            UriCodec.decodeUriConform(canonical.substring(typeNameEndIndex + 1, canonical.length() - 1)));
                }
            }
        }

        throw new IllegalArgumentException("Invalid or unknown canonical format: " + canonical);
    }
}
