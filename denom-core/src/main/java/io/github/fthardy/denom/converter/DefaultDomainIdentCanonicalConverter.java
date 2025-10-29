package io.github.fthardy.denom.converter;

import io.github.fthardy.denom.AtomicIdent;
import io.github.fthardy.denom.CompositeIdent;
import io.github.fthardy.denom.DomainIdent;
import io.github.fthardy.denom.IdentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The default implementation of a domain identifier converter which converts the domain identifier instances into a human-readable string representation and
 * reconverts an identifier vice versa from this representation back into its model representation.
 */
public final class DefaultDomainIdentCanonicalConverter implements DomainIdentCanonicalConverter {

    public static final class CanonicalParseException extends RuntimeException {
        public CanonicalParseException(String message) {
            super(message);
        }
        public CanonicalParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static final char ATOMIC_BEGIN = '(';
    private static final char ATOMIC_END = ')';
    private static final char COMPOSITE_BEGIN = '[';
    private static final char COMPOSITE_END = ']';
    private static final char COMPONENT_SEPARATOR = ',';

    private final DomainIdentFactoryProvider identFactoryProvider;

    /**
     * Creates a new instance of this converter.
     * <p>
     * An instance of {@link DomainIdentFactoryProvider} is created with the parameterless constructor.
     * </p>
     */
    public DefaultDomainIdentCanonicalConverter() {
        this(new DomainIdentFactoryProvider());
    }

    /**
     * Creates a new instance of this converter.
     * <p>
     * Allows to use a factory provider instance which has been created explicitly.
     * </p>
     *
     * @param identFactoryProvider the factory provider instance.
     */
    public DefaultDomainIdentCanonicalConverter(DomainIdentFactoryProvider identFactoryProvider) {
        this.identFactoryProvider = Objects.requireNonNull(identFactoryProvider);
    }

    @Override
    public void assertSupports(IdentType type) {
        identFactoryProvider.getDomainIdentFactory(type);
    }

    @Override
    public String toCanonical(DomainIdent domainIdent) {
        return switch (domainIdent) {
            case AtomicIdent atomicIdent -> //
                    "%s%s%s%s".formatted(domainIdent.type().typeID(), //
                            ATOMIC_BEGIN, //
                            UriCodec.encodeUriConform( //
                                    atomicIdent.getIdentitySequence(), //
                                    "" + COMPONENT_SEPARATOR+ATOMIC_BEGIN+ATOMIC_END+COMPOSITE_BEGIN+COMPOSITE_END), //
                            ATOMIC_END);
            case CompositeIdent compositeIdent -> //
                    "%s%s%s%s".formatted( //
                            domainIdent.type().typeID(), //
                            COMPOSITE_BEGIN, compositeIdent.components().stream() //
                                    .map(this::toCanonical) // recursive call for each component!
                                    .collect(Collectors.joining("" + COMPONENT_SEPARATOR)), //
                            COMPOSITE_END);
        };
    }

    @Override
    public DomainIdent fromCanonical(String canonical) {
        ParserContext parserContext = new ParserContext(canonical);
        try {
            DomainIdent domainIdent = parseDomainIdent(parserContext);
            parserContext.assertEndOfSequence();
            return domainIdent;
        } catch (CanonicalParseException e) {
            throw e;
        } catch (Exception e) {
            throw new CanonicalParseException(Messages.errorWhileParsing(canonical, parserContext.index), e);
        }
    }

    private DomainIdent parseDomainIdent(ParserContext context) {
        IdentType identType = new IdentType(context.parseTypeID());
        DomainIdentFactory domainIdentFactory = identFactoryProvider.getDomainIdentFactory(identType);
        char c = context.peek();
        int contentStart = context.index;
        if (c == DefaultDomainIdentCanonicalConverter.ATOMIC_BEGIN) {
            context.consume(DefaultDomainIdentCanonicalConverter.ATOMIC_BEGIN);
            String identitySequence = context.parseIdentitySequence();
            context.consume(DefaultDomainIdentCanonicalConverter.ATOMIC_END);
            if (domainIdentFactory instanceof AtomicIdentFactory atomicIdentFactory) {
                return atomicIdentFactory.createFrom(identitySequence);
            } else {
                throw new CanonicalParseException(Messages.expectedAtomicIdent(identType.typeID()));
            }
        } else if (c == DefaultDomainIdentCanonicalConverter.COMPOSITE_BEGIN) {
            context.consume(DefaultDomainIdentCanonicalConverter.COMPOSITE_BEGIN);
            List<DomainIdent> identComponents = new ArrayList<>();
            if (context.peek() != DefaultDomainIdentCanonicalConverter.COMPOSITE_END) {
                DomainIdent first = parseDomainIdent(context);
                context.consume(DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR);
                DomainIdent second = parseDomainIdent(context);
                identComponents.add(first);
                identComponents.add(second);
                while (context.peek() == DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR) {
                    context.consume(DefaultDomainIdentCanonicalConverter.COMPONENT_SEPARATOR);
                    identComponents.add(parseDomainIdent(context));
                }
            }
            context.consume(DefaultDomainIdentCanonicalConverter.COMPOSITE_END);
            if (identComponents.isEmpty()) {
                throw new CanonicalParseException(Messages.atLeastTwoComponents(context.canonical, contentStart));
            } else {
                DomainIdent[] further = new DomainIdent[0];
                if (identComponents.size() > 2) {
                    further = identComponents.subList(2, identComponents.size()).toArray(new DomainIdent[0]);
                }
                if (domainIdentFactory instanceof CompositeIdentFactory compositeIdentFactory) {
                    return compositeIdentFactory.createFrom(identComponents.get(0), identComponents.get(1), further);
                } else {
                    throw new CanonicalParseException(Messages.expectedCompositeIdent(identType.typeID()));
                }
            }
        }

        throw new CanonicalParseException(Messages.invalidContentBegin(context.canonical, contentStart));
    }

    static final class ParserContext {

        private final String canonical;
        private int index = 0;

        ParserContext(String canonical) {
            this.canonical = canonical;
        }

        char peek() {
            return canonical.charAt(index);
        }

        char next() {
            return canonical.charAt(index++);
        }

        void consume(char c) {
            char next = next();
            if (next != c) {
                throw new CanonicalParseException(Messages.missingExpectedCharacter(c, canonical, index));
            }
        }

        boolean isNonBoundaryChar(char c) {
            return c != ATOMIC_BEGIN && c != ATOMIC_END && c != COMPOSITE_BEGIN && c != COMPOSITE_END;
        }

        void assertEndOfSequence() {
            if (index != canonical.length()) {
                throw new CanonicalParseException(Messages.expectedEndOfCanonical(canonical, index));
            }
        }

        String parseTypeID() {
            return parseSymbol("type-ID");
        }

        String parseIdentitySequence() {
            return parseSymbol("identity-sequence");
        }

        private String parseSymbol(String name) {
            if (!isNonBoundaryChar(peek())) {
                throw new CanonicalParseException(Messages.invalidStartOfSymbol(peek(), name, index, canonical));
            }
            StringBuilder symbol = new StringBuilder();
            do {
                symbol.append(next());
            } while (isNonBoundaryChar(peek()));
            return symbol.toString();
        }
    }

    static final class Messages {
        private Messages() {}

        static String errorWhileParsing(String canonical, int position) {
            return "Error while parsing at position %d in canonical '%s'!".formatted(position, canonical);
        }

        static String invalidContentBegin(String canonical, int position) {
            return "Expected begin character of domain identifier content at position %d in canonical '%s'!".formatted(position, canonical);
        }

        static String invalidStartOfSymbol(char c, String name, int index, String canonical) {
            return "Invalid character '%s' at begin of expected %s at position %d in canonical %s!".formatted(c, name, index, canonical);
        }

        static String expectedAtomicIdent(String typeID) {
            return "Expected an atomic identifier for type-ID '%s' but seems to be a composite identifier!".formatted(typeID);
        }

        static String expectedCompositeIdent(String typeID) {
            return "Expected a composite identifier for type-ID '%s' but seems to be an atomic identifier!".formatted(typeID);
        }

        static String atLeastTwoComponents(String canonical, int contentStart) {
            return "The composite identifier at position %d in canonical '%s' must have at least two identifier components!".formatted(contentStart, canonical);
        }

        static String missingExpectedCharacter(char c, String canonical, int position) {
            return "Char '%s' expected in canonical '%s' at position %d!".formatted(c, canonical, position);
        }

        static String expectedEndOfCanonical(String canonical, int position) {
            return "Expected end of canonical '%s' at position %d!".formatted(canonical, position);
        }
    }
}
