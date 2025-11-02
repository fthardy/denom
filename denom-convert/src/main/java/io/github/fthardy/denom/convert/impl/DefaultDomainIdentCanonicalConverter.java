package io.github.fthardy.denom.convert.impl;

import io.github.fthardy.denom.AtomicIdent;
import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.convert.DomainIdentCanonicalConverter;
import io.github.fthardy.denom.convert.util.AtomicIdentCanonicalConverter;
import io.github.fthardy.denom.convert.util.CompositeIdentFactory;
import io.github.fthardy.denom.convert.util.DomainIdentCanonicalConverterSupportRegistry;
import io.github.fthardy.denom.convert.util.DomainIdentConvertSupport;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * The default implementation of a domain identifier converter which converts the domain identifier instances into a human-readable string representation and
 * reconverts an identifier vice versa from this representation back into its model representation.
 */
public final class DefaultDomainIdentCanonicalConverter implements DomainIdentCanonicalConverter {

    static final char CONTENT_BEGIN = '(';
    static final char CONTENT_END = ')';
    static final char COMPONENT_SEPARATOR = ',';

    private final DomainIdentCanonicalConverterSupportRegistry supportRegistry;
    private final CanonicalDomainIdentParser parser;

    public DefaultDomainIdentCanonicalConverter(DomainIdentCanonicalConverterSupportRegistry registry) {
        this.supportRegistry = registry;
        this.parser = new CanonicalDomainIdentParser(registry);
    }

    @Override
    public String toCanonical(DomainIdent domainIdent) {
        String canonical;
        DomainIdentConvertSupport<?> support = supportRegistry.getSupportByClass(domainIdent.getClass());
        if (support == null) {
            throw new NoSuchElementException(Messages.unknownTypeClass(domainIdent.getClass()));
        }
        switch (domainIdent) {
            case AtomicIdent<?> atomicIdent -> { //
                AtomicIdentCanonicalConverter<?> atomicConverter = (AtomicIdentCanonicalConverter<?>) support;
                canonical = "%s%s%s%s".formatted(atomicConverter.typeClass2AliasBinding().typeAlias(), //
                        CONTENT_BEGIN, //
                        UriCodec.encodeUriConform( //
                                atomicConverter.toCanonical(atomicIdent), "" + COMPONENT_SEPARATOR + CONTENT_BEGIN + CONTENT_END), //
                        CONTENT_END);
            }
            case CompositeIdent compositeIdent -> { //
                CompositeIdentFactory<?> compositeConverter = (CompositeIdentFactory<?>) support;
                canonical = "%s%s%s%s".formatted( //
                        compositeConverter.typeClass2AliasBinding().typeAlias(), //
                        CONTENT_BEGIN, compositeIdent.components().stream() //
                                .map(this::toCanonical) // recursive call for each component!
                                .collect(Collectors.joining("" + COMPONENT_SEPARATOR)), //
                        CONTENT_END);
            }
        }

        return canonical;
    }

    @Override
    public DomainIdent fromCanonical(String canonical) {
        return parser.parseDomainIdentFrom(canonical);
    }

    static final class Messages {

        private Messages() {}

        static String unknownTypeClass(Class<? extends DomainIdent> aClass) {
            return "No convert-support-implementation found for domain identifier class: %s".formatted(aClass.getName());
        }
    }
}
