package io.github.fthardy.denom.convert.impl;

import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.convert.util.AtomicIdentCanonicalConverter;
import io.github.fthardy.denom.convert.util.CompositeIdentFactory;
import io.github.fthardy.denom.convert.util.DomainIdentCanonicalConverterSupportRegistry;
import io.github.fthardy.denom.convert.util.DomainIdentConvertSupport;

import java.util.ArrayList;
import java.util.List;

final class CanonicalDomainIdentParser {

    private final DomainIdentCanonicalConverterSupportRegistry supportRegistry;

    CanonicalDomainIdentParser(DomainIdentCanonicalConverterSupportRegistry registry) {
        this.supportRegistry = registry;
    }

    public DomainIdent parseDomainIdentFrom(String canonical) {
        CanonicalParseContext context = new CanonicalParseContext(canonical);
        try {
            DomainIdent domainIdent = parse(context);
            context.assertEndOfSequence();
            return domainIdent;
        } catch (CanonicalParseException e) {
            throw e;
        } catch (Exception e) {
            throw new CanonicalParseException(Messages.parsingFailed(canonical, context.getIndex()), e);
        }

    }

    private DomainIdent parse(CanonicalParseContext context) {
        final int contentStart = context.getIndex();
        final DomainIdentConvertSupport<?> converterSupport = parseTypeAliasAndGetConverterSupport(context);
        final char c = context.peek();
        if (c == DefaultDomainIdentCanonicalConverter.CONTENT_BEGIN) {
            context.consume(DefaultDomainIdentCanonicalConverter.CONTENT_BEGIN);
            DomainIdent domainIdent;
            if (converterSupport instanceof AtomicIdentCanonicalConverter<?> converter) {
                domainIdent = converter.fromCanonical(context.parseIdentityValue());
            } else if (converterSupport instanceof CompositeIdentFactory<?> factory) {
                domainIdent = parseCompositeIdent(context, factory);
            } else {
                throw new IllegalStateException("Unhandled converterSupport type: " + converterSupport.getClass().getName());
            }
            context.consume(DefaultDomainIdentCanonicalConverter.CONTENT_END);
            return domainIdent;
        }
        throw new CanonicalParseException(Messages.invalidContentBegin(context.getCanonical(), contentStart));
    }

    private DomainIdentConvertSupport<?> parseTypeAliasAndGetConverterSupport(CanonicalParseContext context) {
        int aliasStart = context.getIndex();
        String typeAlias = context.parseTypeAlias();
        DomainIdentConvertSupport<?> converterSupport = supportRegistry.getSupportByTypeAlias(typeAlias);
        if (converterSupport == null) {
            throw new CanonicalParseException(Messages.unknownTypeAlias(typeAlias, context.getCanonical(), aliasStart));
        }
        return converterSupport;
    }

    private CompositeIdent parseCompositeIdent(CanonicalParseContext context, CompositeIdentFactory<?> factory) {
        int contentStart = context.getIndex();
        List<DomainIdent> identComponents = new ArrayList<>();
        if (context.peek() != DefaultDomainIdentCanonicalConverter.CONTENT_END) {
            DomainIdent first = parse(context);
            context.consume(DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR);
            DomainIdent second = parse(context);
            identComponents.add(first);
            identComponents.add(second);
            while (context.peek() == DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR) {
                context.consume(DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR);
                identComponents.add(parse(context));
            }
        }

        if (identComponents.isEmpty()) {
            throw new CanonicalParseException(Messages.atLeastTwoComponents(context.getCanonical(), contentStart));
        }

        DomainIdent[] further = new DomainIdent[0];
        if (identComponents.size() > 2) {
            further = identComponents.subList(2, identComponents.size()).toArray(new DomainIdent[0]);
        }
        return factory.fromComponents(identComponents.get(0), identComponents.get(1), further);
    }

    static final class Messages {

        private Messages() {}

        static String invalidContentBegin(String canonical, int position) {
            return "Expected begin character of domain identifier content at position %d in canonical '%s'!".formatted(position, canonical);
        }

        static String atLeastTwoComponents(String canonical, int contentStart) {
            return "The composite identifier at position %d in canonical '%s' must have at least two identifier components!".formatted(contentStart, canonical);
        }

        public static String unknownTypeAlias(String typeAlias, String canonical, int position) {
            return "The type alias '%s' at position %d in canonical '%s' is unknown!".formatted(typeAlias, position, canonical);
        }

        static String parsingFailed(String canonical, int position) {
            return "Parsing failed at position %d in canonical '%s'!".formatted(position, canonical);
        }
    }
}
